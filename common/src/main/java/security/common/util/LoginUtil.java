package security.common.util;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class LoginUtil {

    public static boolean isLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.equals(principal.toString(), "anonymousUser")) {
            return false;
        } else {
            return true;
        }
    }

//    public static MbrInfo getMbrInfo() {
//
//    }
}
