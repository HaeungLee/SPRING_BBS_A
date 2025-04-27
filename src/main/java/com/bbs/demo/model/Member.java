package com.bbs.demo.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Member {
	
	private Integer userId;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phone;
    private String profileImg; // 프로필 이미지
    private boolean agreeMarketing;  // 마케팅 정보 수신 동의
    private String isManager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}