package com.bbs.demo.service;

import java.util.List;
import com.bbs.demo.model.Post;

public interface PostService {
    List<Post> getAllPosts();                 // 게시글 목록 조회
    Post getPostById(int posts_id);           // 게시글 상세 조회
    void createPost(Post post, int currentUserId);               // 게시글 등록
    void updatePost(Post post, int currentUserId);               // 게시글 수정
    void deletePost(int posts_id, int currentUserId);            // 게시글 삭제
    void incrementViewCount(int posts_id, int currentUserId);    // 조회수 증가
}
