package security.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import security.common.model.MbrInfo;
import security.common.service.LoginService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mypage")
public class MypageController {

    private final LoginService loginService;

    @GetMapping("initMemberInfo")
    public String initMemberInfo(Model model) {

        MbrInfo mbrInfo = loginService.getMbrInfo();
        model.addAttribute("mbrInfo", mbrInfo);
        log.info("model = {}", model);
        return "mypage/member_info";
    }
}
