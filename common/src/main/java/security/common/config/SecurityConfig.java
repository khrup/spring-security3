package security.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import security.common.auth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity//해당 어노테이션을 선언하면 CSRF 공격을 방지하는 기능을 지원하고 있음.
/*
    CSRF : 웹 애플리케이션의 취약점 중 하나로 이용자가 의도하지 않은 요청을 통한 공격을 의미한다.
           (ex. 고객이 돈을 이체 할때 중간에 이벤트를 가로채어 계좌번호를 바꾸는 경우)
    Spring Security 에서는 CSRF Token 방식으로 CSRF 공격을 방어함.
    data를 전송할 때 CSRF Token 이 존재 하지 않거나 기존의 Token값과 일치하지 않는 경우 4XX 상태코드를 리턴한다.

    Cookie HttpOnly : true가 적용되면 document.cookie로 접근 할 수 없다.
    그리고 서버로 HTTP Request 요청을 보낼 때만 쿠키를 전송한다.

    Secure Cookie : HTTPS 로 통신하는 경우에만 웹브라우저가 쿠키를 서버로 전송하는 옵션
 */
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    final private CustomAuthenticationFailHandler customAuthenticationFailHandler;
    final private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/session").authenticated()//이 주소로 들어오면 인증이 필요
                .antMatchers("/mypage/*").authenticated()
                .anyRequest().permitAll()
            .and()
                .formLogin()
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginPage("/login")//login 주소
                .loginProcessingUrl("/login")//login 주소가 post 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailHandler)
            .and()
//                .logout()    // 로그아웃 사용
//                .logoutSuccessUrl("/")    //로그아웃 시 가지는 페이지
//                .invalidateHttpSession(true)    // 세션 초기화
//                .deleteCookies("SESSION");	//쿠키 삭제
//                .and()
            .oauth2Login()
                .failureHandler(customAuthenticationFailHandler)
                .successHandler(customAuthenticationSuccessHandler)
                .loginPage("/login") //oauth2.0 로그인 페이지, 네이버 로그인이 완료된뒤의 후처리가 필요함
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()


        ;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());//정적 소스들은 무시해준다.
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
