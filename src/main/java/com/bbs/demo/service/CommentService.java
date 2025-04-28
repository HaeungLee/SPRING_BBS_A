package com.bbs.demo.service;

import java.util.List;

import com.bbs.demo.model.Comment;

public interface CommentService {
    List<Comment> getCommentsByPostId(int postId);
    void createComment(Comment comment);
    List<Comment> getRepliesByParentId(int parentId);
    boolean likeComment(int commentId);
    boolean unlikeComment(int commentId);
    boolean deleteComment(int id, int userId);
    boolean updateComment(Comment comment, int userId);
}