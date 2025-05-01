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
	private Double lat;   // 위도
    private Double lng;   // 경도
	
	private String username;
	private String nickname;
	
	// 파일 리스트 추가
	private List<FileInfo> files = new ArrayList<>();
	
	// 썸네일 이미지 ID (DB 컬럼 아님, 뷰에서 표시용)
	private Integer thumbnailId;
	private String thumbnailUrl;
	
	private int commentCount;
}
