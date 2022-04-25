package security.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import security.common.model.MbrInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String headerXmlRequestedWith = request.getHeader("X-Requested-With");
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response); //클라이언트가 요청과정에서 발생한 여러 정보들을 저장하는 클래스
        String redirectUrl = !ObjectUtils.isEmpty(savedRequest) ? savedRequest.getRedirectUrl() : "/";

        log.info("success");

        //request header에 X-Requested-With 값이 "XMLHttpRequest" -> ajax 요청
        if ("XMLHttpRequest".equals(headerXmlRequestedWith)) {
            ObjectMapper mapper = new ObjectMapper();    //JSON 변경용

            MbrInfo mbrInfo = (MbrInfo) authentication.getPrincipal();

            Map<String, Object> items = new HashMap<>();
            items.put("redirectUrl", redirectUrl);
            items.put("userName", mbrInfo.getUsername());

            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(mapper.writeValueAsString(items));
            response.getWriter().flush();
        } else {
            response.sendRedirect(redirectUrl);
        }
    }
}
