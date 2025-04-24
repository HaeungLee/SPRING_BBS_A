package com.bbs.demo.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service  // Spring의 서비스로 등록되어 Bean으로 관리
public class MemberSecurityService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      
        if ("admin".equals(username)) {
            
            return User.builder()
                    .username("admin")  
                    .password("{noop}admin123")  
                    .roles(users.isAdmin() ? "ADMIN" : "USER") 
                    .build();  
        }
        // 사용자 이름이 "admin"이 아니면 UsernameNotFoundException 예외를 던짐
        // 이는 Spring Security가 해당 사용자 이름으로 로그인할 수 없음
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
    }
}

