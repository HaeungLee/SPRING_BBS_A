package com.bbs.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bbs.demo.mapper.MemberMapper;
import com.bbs.demo.model.Member;

@Component
public class PasswordMigrationService {

    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Transactional
    public void migratePasswords() {
        List<Member> members = memberMapper.getAllMembers();
        for (Member member : members) {
            // 암호화되지 않은 비밀번호 확인 (예: 길이나 특정 패턴)
            if (isPlainTextPassword(member.getPassword())) {
                String encodedPassword = passwordEncoder.encode(member.getPassword());
                member.setPassword(encodedPassword);
                memberMapper.updateMember(member); // MemberMapper 인터페이스에 맞게 수정
            }
        }
    }
    
    private boolean isPlainTextPassword(String password) {
        // 암호화된 비밀번호는 보통 $2a$로 시작 (BCrypt)
        return !password.startsWith("$2a$");
    }
}