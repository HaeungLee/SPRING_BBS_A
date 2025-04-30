package com.bbs.demo.service.impl;

import java.util.List;
import java.util.Objects;

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
        
        // 관리자는 모든 댓글 삭제 가능 (userId가 0이면 관리자로 간주)
        if (userId == 0) {
            commentMapper.markCommentAsDeleted(id, userId);
            deleteChildCommentsRecursively(id, userId);
            return true;
        }
        
        // 작성자 ID가 null이거나 삭제 요청자 ID와 일치하는 경우
        if (existing != null && (existing.getUser_id() == null || Objects.equals(existing.getUser_id(), userId))) {
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
            // 재귀적으로 자식 댓글들도 삭제 처리
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