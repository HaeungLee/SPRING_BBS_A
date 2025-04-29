package com.bbs.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
  
    public SecurityConfig(@Lazy UserDetailsService userDetailsService, CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    // 비밀번호 인코더 설정
        @Bean
        public PasswordEncoder passwordEncoder() {
            // 암호화 방식 설정
            Map<String, PasswordEncoder> encoders = new HashMap<>();
            encoders.put("bcrypt", new BCryptPasswordEncoder());

            // 기본 암호화 방식으로 bcrypt 사용하도록 설정
            DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);

            // 기본 비밀번호 암호화 방식으로 bcrypt 설정
            delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());

            // 비밀번호를 저장할 때 반드시 {bcrypt} 접두사를 붙여주기 위해
            return delegatingPasswordEncoder;
        }

    // 시큐리티 필터 체인 (권한, 로그인, 로그아웃 설정)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자만 접근
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()  // 모두 접근 가능
                .anyRequest().authenticated()  // 그 외는 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/login")  // 커스텀 로그인 페이지
                .loginProcessingUrl("/login")  // 로그인 POST 처리 URL
                .successHandler(authenticationSuccessHandler)  // 로그인 성공 시 커스텀 핸들러 사용
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")  // 로그아웃 후 홈으로 리디렉션
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());  // CSRF 비활성화, API나 테스트 목적이면 꺼도 됨

        return http.build();
    }
    
    // 인증 매니저
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // HttpSecurity에서 AuthenticationManagerBuilder 객체를 가져오기
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // 사용자 세부 정보를 가져오는 userDetailsService와 비밀번호를 인코딩하는 passwordEncoder 설정
        authBuilder.userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder());

        // 설정을 마친 후, AuthenticationManager를 빌드하여 반환
        return authBuilder.build();
    }

}
