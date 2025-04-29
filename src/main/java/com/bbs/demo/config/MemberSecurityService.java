package com.bbs.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bbs.demo.model.Users;
import com.bbs.demo.service.UsersService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {

    @Lazy
    private UsersService usersService; // DB에서 사용자 정보를 가져오는 서비스
    
    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersService.getUserByUsername(username);

        if (user == null) {
        	System.out.println("로그인 안되요");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        String role = "USER";
        if ("Y".equals(user.getIs_Manager())) {
        	
            role = "ADMIN";
            
        }

        System.out.println("role : " + role);
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // BCrypt로 암호화된 비밀번호 그대로
                .roles(role) // "USER" 또는 "ADMIN"
                .build();
    }
}
