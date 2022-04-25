package security.common.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import security.common.model.MbrInfo;
import security.common.repository.LoginRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    final private LoginRepository loginRepository;

    //구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration = {}", userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인했는지 확인가능.
        log.info("getAccessToken = {}", userRequest.getAccessToken().getTokenValue()); //getAttributes에 데이터가 이미 존재하여 필요없는 토큰값이다.
        log.info("getAdditionalParameters = {}", userRequest.getAdditionalParameters());

        OAuth2User oauth2User = super.loadUser(userRequest);
        //구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        //userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필을 받아준다.
        log.info("getAttributes = {}", oauth2User.getAttributes());

        MbrInfo mbrInfo = new MbrInfo();
        mbrInfo.setTokenValue(userRequest.getAccessToken().getTokenValue());

        MbrInfo mbrInfoOAuth = loginRepository.getMbrInfoOAuth(mbrInfo);
        log.info("mbrInfoOAuth = {}", mbrInfoOAuth);
        if(mbrInfoOAuth!= null) {
            return new MemberUserDetails(mbrInfoOAuth, oauth2User.getAttributes());
        }else
            return null;
    }
}
