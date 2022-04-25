package security.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import security.common.model.MbrInfo;
import security.common.repository.LoginRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    final private LoginRepository loginRepository;

    public int createMbrInfo(MbrInfo member) {
        return loginRepository.createMbrInfo(member);
    }

    public List<MbrInfo> findAllMbrInfo() {
        return loginRepository.findAllMbrInfo();
    }

    public MbrInfo getMbrInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MbrInfo mbrInfo = (MbrInfo) principal;
        return loginRepository.getMbrInfo(mbrInfo);
    }
}