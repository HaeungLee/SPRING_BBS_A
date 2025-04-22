package com.bbs.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bbs.demo.model.Comment;

@Mapper
public interface CommentMapper {
	
	List<Comment> getCommentsByPostId(@Param("postId") int postId);
	int insertComment(Comment comment);
    int updateComment(Comment comment);
    int deleteComment(@Param("id") int id, @Param("userId") int userId);
    Comment getCommentById(int id);
    
}
