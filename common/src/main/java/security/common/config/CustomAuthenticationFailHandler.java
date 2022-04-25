package security.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String headerXmlRequestedWith = request.getHeader("X-Requested-With");

        //request header에 X-Requested-With 값이 "XMLHttpRequest" -> ajax 요청
        if("XMLHttpRequest".equals(headerXmlRequestedWith)){
            ObjectMapper mapper = new ObjectMapper();	//JSON 변경용

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("error_message", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(mapper.writeValueAsString(resultMap));
            response.getWriter().flush();
        }
        log.info("로그인 실패");
    }
}
