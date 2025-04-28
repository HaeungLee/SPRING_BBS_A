package com.bbs.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class PasswordEncodingTest {

	    public static void main(String[] args) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String rawPassword = "1234";  // 네가 암호화하고 싶은 평문 비밀번호
	        String encodedPassword = passwordEncoder.encode(rawPassword);

	        System.out.println("암호화된 비밀번호: " + encodedPassword);
	    }
	}

