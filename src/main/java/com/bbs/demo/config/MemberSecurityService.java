package com.bbs.demo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bbs.demo.mapper.MemberMapper;
import com.bbs.demo.model.Member;

@Service
public class MemberSecurityService implements UserDetailsService {
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberMapper.findByUsername(username);
        
        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        
        // 권한 설정 - isManager가 "Y"면 ROLE_ADMIN, 아니면 ROLE_USER
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        if ("Y".equals(member.getIsManager())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        
        // Spring Security User 객체 생성
        // UserDetails 객체는 세션에 저장됨
        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}