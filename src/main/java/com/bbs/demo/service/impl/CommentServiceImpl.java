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
    public List<Comment> getAllComments() {
        return commentMapper.getAllComments();
    }

    @Override
    public void createComment(Comment comment) {
        commentMapper.insertComment(comment);
    }

    @Override
    public boolean updateComment(Comment comment, int userId) {
        Comment existing = commentMapper.getCommentById(comment.getComment_id());
        if (existing != null && existing.getUser_id().equals(userId)) {
            commentMapper.updateComment(comment);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteComment(int id, int userId) {
        Comment existing = commentMapper.getCommentById(id);
        if (existing != null && existing.getUser_id().equals(userId)) {
            commentMapper.markCommentAsDeleted(id, userId);
            deleteChildCommentsRecursively(id, userId);
            return true;
        }
        return false;
    }
    
    private void deleteChildCommentsRecursively(int parentId, int userId) {
        List<Integer> childIds = commentMapper.getChildCommentIds(parentId);
        for (Integer childId : childIds) {
            commentMapper.markCommentAsDeleted(childId, userId);
            deleteChildCommentsRecursively(childId, userId);
        }
    }
    
    @Override
    public void toggleLike(int commentId, int userId) {
        boolean liked = commentMapper.hasLiked(commentId, userId);
        if (liked) {
            commentMapper.deleteLike(commentId, userId);
            commentMapper.decrementLikeCount(commentId);
        } else {
            commentMapper.insertLike(commentId, userId);
            commentMapper.incrementLikeCount(commentId);
        }
    }
}