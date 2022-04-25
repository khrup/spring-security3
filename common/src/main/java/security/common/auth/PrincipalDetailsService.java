package security.common.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.common.model.MbrInfo;
import security.common.repository.LoginRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        MbrInfo tmp = new MbrInfo();
        tmp.setLoginId(loginId);
        MbrInfo mbrInfo = loginRepository.getMbrInfo(tmp);
        if (mbrInfo != null) {
            return mbrInfo;
        }

        return null;
    }
}
