package com.bbs.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbs.demo.mapper.CommentMapper;
import com.bbs.demo.model.Comment;
import com.bbs.demo.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByPostId(int postId) {
        return commentMapper.getCommentsByPostId(postId);
    }

    @Override
    public void createComment(Comment comment) {
        commentMapper.insertComment(comment);
    }

    @Override
    public boolean updateComment(Comment comment, int userId) {
        Comment existing = commentMapper.getCommentById(comment.getComment_id());
        if (existing != null && userId == existing.getUser_id()) {
            commentMapper.updateComment(comment);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteComment(int id, int userId) {
        Comment existing = commentMapper.getCommentById(id);
        if (existing != null && userId == existing.getUser_id()) {
            commentMapper.markCommentAsDeleted(id, userId);
            return true;
        }
        return false;
    }

    @Override
    public List<Comment> getRepliesByParentId(int parentId) {
        return commentMapper.getRepliesByParentId(parentId);
    }

    @Override
    public boolean likeComment(int commentId) {
        return commentMapper.incrementLikeCount(commentId) > 0;
    }

    @Override
    public boolean unlikeComment(int commentId) {
        return commentMapper.decrementLikeCount(commentId) > 0;
    }
}