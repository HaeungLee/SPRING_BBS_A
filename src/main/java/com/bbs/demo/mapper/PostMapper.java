package com.bbs.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bbs.demo.model.Post;

@Mapper
public interface PostMapper {
    
	List<Post> findPosts(@Param("offset") int offset, @Param("limit") int limit);                   // 게시글 전체 조회
    
	List<Post> searchPosts(@Param("type") String type,   // 검색 포스트 조회
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit);

	List<String> suggestTitles(String keyword);

	
    Post getPostById(Integer post_id);         // 특정 게시글 조회(상세보기)
    
    void insertPost(Post post);                 // 게시글 삽입
    
    void updatePost(Post post);                 // 게시글 수정
    
    void deletePost(Integer post_id);          // 게시글 삭제

    void incrementViewCount(Integer post_id);  // 조회수 증가
}
