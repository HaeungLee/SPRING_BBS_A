package com.bbs.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthSuccessHandler authSuccessHandler;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // 정적 리소스 접근 허용
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // 누구나 접근 가능한 페이지
                .requestMatchers("/", "/login", "/register", "/logout", "/post/list", "/post/view/**").permitAll()
                // API 요청 - 댓글 조회는 누구나 가능하지만 등록/수정/삭제는 인증 필요
                .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()
                // 관리자 페이지 접근 제한
                .requestMatchers("/admin/index", "/admin/users").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/admin/posts", "/admin/comments", "/admin/files").hasAuthority("ROLE_ADMIN")
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(authSuccessHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            )
            // CSRF 보호 비활성화 (AJAX 요청에 영향 없게)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
        
        return http.build();
    }
}