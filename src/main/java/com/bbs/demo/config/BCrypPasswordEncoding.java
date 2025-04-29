package com.bbs.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class BCrypPasswordEncoding {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void encodePasswordExample() {
        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("비밀번호: " + encodedPassword);
    }
} 
