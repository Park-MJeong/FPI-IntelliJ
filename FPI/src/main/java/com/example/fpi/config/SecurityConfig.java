package com.example.fpi.config;

import com.example.fpi.domain.dto.user.UserDTO;
import com.example.fpi.domain.oauth.CustomOAuth2User;
import com.example.fpi.mapper.user.UserMapper;
import com.example.fpi.service.user.CustomOAuth2UserService;
import com.example.fpi.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

//수동으로 스프링컨테이너에 빈 등록 @configuration 안에 @bean 사용
//@configuration 안의 @component가 클래스를 자동으로 찾아서 빈을 등록해줌 ==>싱글톤(항상 동일한 객체 반환) 보장

@Configuration
@EnableWebSecurity //웹 보안 활성화 ,spring security
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserMapper userMapper;
    private final UserService userService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 전체 요청에 접근할 수 있도록 하는 코드
//        return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).build();
        return http
//                csrf 비활성화 : post요청시 403에러,
                .csrf(AbstractHttpConfigurer::disable)
                //요청에 대한 인증 및 인가를 설정.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                //로그인을 OAuth2으로 구성할 것이다.
                .oauth2Login(login->login
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        ).successHandler(authenticationSuccessHandler())


                )

                .logout(logout->logout
                                .logoutSuccessHandler((request, response, authentication)->{
//                            앱키 : REST API키 복붙
                                    String clientId="6d8634b2b70d1f0713825ca4b0c881e6";
                                    String logoutRedirectUri="http://localhost:8070/main";
                                    String logoutUri="https://kauth.kakao.com/oauth/logout?client_id="
                                            + clientId + "&logout_redirect_uri=" + logoutRedirectUri;
//                                    request.getSession().invalidate();
                                    response.sendRedirect(logoutUri);
                                })
                )

                .build();

    }

//    스프링시큐리티가 인증성공시 successhandler호출
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, auth) -> {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) auth.getPrincipal();
            UserDTO user = userMapper.findByUserId(customOAuth2User.getUserId());
            HttpSession session = request.getSession();

//            회원의 role값이 new라면 정보가 없는 회원이여서 회원가입 폼으로 이동
            if(user.getRole().equals("new")){
                response.sendRedirect("/main/sign");
            }
            else {
//                홈페이지 이동시 헤더에서 계속 정보를 가지고 있어야 하기 때문에 로그인시 session에 담아줌
                session.setAttribute("loginName", userMapper.findByUserId(user.getUserId()).getUserName());
//                전문가전환 버튼유무에 필요
                session.setAttribute("userProApproval",userService.detailUser(user.getUserId()).getUserProApproval());
                response.sendRedirect("/main/user");
            }
        };
    }
}