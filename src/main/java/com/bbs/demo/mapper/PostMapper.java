package com.bbs.demo.mapper;

import java.util.List;

import com.bbs.demo.model.Post;

public interface PostMapper {
    
	List<Post>getAllPosts();				// 게시글 전체 조회
	
	Post getPostById(Integer posts_id);		// 특정 게시글 조회(상세보기)
	
	void insertPost(Post post);				// 게시글 삽입
	
	void updatePost(Post post);				// 게시글 수정
	
	void deletePost(Integer posts_id);		// 게시글 삭제
}
