package com.bbs.demo.service;

import java.util.List;

import com.bbs.demo.model.Comment;

public interface CommentService {
    List<Comment> getCommentsByPostId(int postId);
    List<Comment> getAllComments();
    void createComment(Comment comment);
    boolean deleteComment(int id, int userId);
    boolean updateComment(Comment comment, int userId);
    void toggleLike(int commentId, int userId);
}
