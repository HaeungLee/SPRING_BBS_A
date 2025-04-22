package com.bbs.demo.model;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class Post {
	
	private Integer posts_id;
	private String file;
	private String content;
	private String region;
	private Integer views;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private Integer Users_id;
	private boolean isNotice;
	
}
