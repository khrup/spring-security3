package security.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import security.common.model.MbrInfo;

@RestController
@Slf4j
public class ApiController {

    @PostMapping("api")
    public MbrInfo apiPost() {
        MbrInfo mbrInfo = (MbrInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("mbrInfo = {}", mbrInfo);

        return mbrInfo;
    }

    @GetMapping("api")
    public MbrInfo apiGet() {
        MbrInfo mbrInfo = (MbrInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("mbrInfo = {}", mbrInfo);
        return mbrInfo;
    }
}
