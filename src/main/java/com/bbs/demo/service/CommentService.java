package com.bbs.demo.service;

import java.util.List;

import com.bbs.demo.model.Comment;

public interface CommentService {
    
	List<Comment> getCommentsByPostId(int postId);
	void createComment(Comment comment);
	boolean updateComment(Comment comment, int userId);
	boolean deleteComment(int id, int userId);
}
