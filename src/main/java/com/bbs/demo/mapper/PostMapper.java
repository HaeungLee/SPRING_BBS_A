package com.bbs.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bbs.demo.model.Post;

@Mapper
public interface PostMapper {
    
    List<Post> getAllPosts();                   // 게시글 전체 조회
    
    Post getPostById(Integer post_id);         // 특정 게시글 조회(상세보기)
    
    void insertPost(Post post);                 // 게시글 삽입
    
    void updatePost(Post post);                 // 게시글 수정
    
    void deletePost(Integer post_id);          // 게시글 삭제

    void incrementViewCount(Integer post_id);  // 조회수 증가
}
