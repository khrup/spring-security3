package security.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import security.common.model.MbrInfo;
import security.common.service.LoginService;
import security.common.util.LoginUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginService loginService;

    @GetMapping("/login")
    public String login() {
        return "login/Login";
    }

    @GetMapping("/loginAjax")
    public String loginAjax() {
        return "login/LoginAjax";
    }

    @GetMapping("/memberJoinForm")
    public String memberJoinForm() {
        return "login/MemberJoinForm";
    }

    @PostMapping("/member")
    public String createMember(@ModelAttribute MbrInfo member) {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        int successCnt = loginService.createMbrInfo(member);
        if (successCnt > 0) {
            return "login/Login";
        } else {
            return "login/MemberJoinForm";
        }
    }

    @GetMapping("session")
    @ResponseBody
    public String session(HttpServletRequest request) {

        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        Cookie[] cookies = request.getCookies();

        String csrf = "";
        if (!ObjectUtils.isEmpty(cookies)) {
            String cookieString = "";
            for (Cookie cookie : cookies) {
                cookieString += cookie.getName() + "=" + cookie.getValue() + ";";
                if ("XSRF-TOKEN".equals(cookie.getName())) {
                    csrf = cookie.getValue();
                }
            }
            headers.set("Cookie", cookieString);
//            headers.set("X-XSRF-TOKEN", csrf);
        }
        //MultiValueMap : key에 여러 값을 지정할 수있음
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("_csrf", csrf);

        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);

        log.info("req = {}", req);

        Map<String, Object> mbrInfo = rest.postForObject("http://localhost:10001/api", req, Map.class);
        log.info("mbrInfo = {}", mbrInfo);
        return "ok";
    }

    @PostMapping("session")
    @ResponseBody
    public MbrInfo sessionPost(@AuthenticationPrincipal MbrInfo mbrInfo) {

        log.info("AuthenticationPrincipal = {}", mbrInfo);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mbrInfo;
    }

    @GetMapping("sessionTest")
    @ResponseBody
    public String sessionTest(@AuthenticationPrincipal MbrInfo mbrInfo, HttpServletRequest request) {

        log.info("AuthenticationPrincipal = {}", mbrInfo);

        log.info("getContext = {}", SecurityContextHolder.getContext());
        log.info("getAuthentication = {}", SecurityContextHolder.getContext().getAuthentication());
        log.info("getPrincipal = {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        log.info("getPrincipal class = {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass());
        log.info("getDetails = {}", SecurityContextHolder.getContext().getAuthentication().getDetails());
        log.info("isLogin = {}", LoginUtil.isLogin());

        return "ok";
    }

    @GetMapping("sessionSelect")
    @ResponseBody
    public String sessionSelect(HttpServletRequest request) {

        HttpSession session = request.getSession();

        Object securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");

        log.info("securityContext = {}", securityContext);

        session.setAttribute("SPRING_SECURITY_CONTEXT", "123");

        log.info("update Session = {}", session.getAttribute("SPRING_SECURITY_CONTEXT"));


        return "ok";
    }
}

