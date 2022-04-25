package security.common.auth;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import security.common.model.MbrInfo;

import java.util.Collection;
import java.util.Map;

@ToString
public class MemberUserDetails implements UserDetails, OAuth2User {

    private MbrInfo member;
    private Map<String, Object> attributes;

    //일반 로그인
    public MemberUserDetails(MbrInfo member) {
        this.member = member;
    }

    //OAuth 로그인
    public MemberUserDetails(MbrInfo member, Map<String, Object> attributes){
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //계정이 만료되었는지, false -> 계정 만료
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //계정이 잠겼는지 false -> 계정 잠김
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;//비밀번호가 만료되었는지 false -> 만료
    }

    @Override
    public boolean isEnabled() {
        return true; //계정이 활성화 됐는지 , false -> 계정 비활성화
    }

    @Override
    public String getName() {
        return null;
    }
}
