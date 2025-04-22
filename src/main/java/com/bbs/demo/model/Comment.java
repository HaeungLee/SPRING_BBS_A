package com.bbs.demo.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Comment {
	
	private Integer commentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer userId;
    private Integer postId;
}