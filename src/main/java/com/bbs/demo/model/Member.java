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
    private String isManager;
    private LocalDateTime createdAt;  
}