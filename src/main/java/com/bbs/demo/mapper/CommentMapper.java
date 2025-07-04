package com.bbs.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bbs.demo.model.Comment;

@Mapper
public interface CommentMapper {

    List<Comment> getCommentsByPostId(@Param("post_id") int postId);

    List<Comment> getRepliesByParentId(@Param("parent_id") int parentId);
    
    List<Comment> getAllComments();

    int insertComment(Comment comment);

    int updateComment(Comment comment);

    int markCommentAsDeleted(@Param("id") int id, @Param("user_id") int userId);

    Comment getCommentById(@Param("id") int id);

    int incrementLikeCount(@Param("id") int id);

    int decrementLikeCount(@Param("id") int id);

    boolean hasLiked(@Param("commentId") int commentId, @Param("userId") int userId);

    void insertLike(@Param("commentId") int commentId, @Param("userId") int userId);

    void deleteLike(@Param("commentId") int commentId, @Param("userId") int userId);
    
    List<Integer> getChildCommentIds(@Param("parent_id") int parentId);

}


