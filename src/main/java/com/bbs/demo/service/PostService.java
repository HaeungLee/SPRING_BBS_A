package com.bbs.demo.service;

import java.util.List;
import com.bbs.demo.model.Post;

public interface PostService {
	List<Post> getPosts(int offset, int limit);                  // 게시글 목록 조회
	List<Post> searchPosts(String type, String keyword, int offset, int limit);  // 검색 포스트 조회
    List<String> suggestTitles(String keyword);
    Post getPostWithViewCount(int post_id, int currentUserId);  // 조회수 증가 포함 메서드(작성자 본인체크)
    Post getPostById(int post_id);           // 게시글 상세 조회
    void createPost(Post post, int currentUserId);               // 게시글 등록
    void updatePost(Post post, int currentUserId);               // 게시글 수정
    void deletePost(int post_id, int currentUserId);            // 게시글 삭제
    void incrementViewCount(int post_id, int currentUserId);    // 조회수 증가
}
