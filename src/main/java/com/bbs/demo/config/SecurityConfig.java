package com.bbs.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfig(UserDetailsService userDetailsService, CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    // 비밀번호 인코더 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }
}
