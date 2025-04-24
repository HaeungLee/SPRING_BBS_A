package com.bbs.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class Post {
	
	private Integer post_id;
	private String title;
	private String content;
	private String region;
	private Integer views;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private Integer user_id;
	private boolean isNotice;
	
	private String username;
	
	// 파일 리스트 추가
	private List<FileInfo> files = new ArrayList<>();
}
