package com.bbs.demo.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Comment {
	
	private Integer comment_id;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Integer user_id;
    private Integer post_id;
}