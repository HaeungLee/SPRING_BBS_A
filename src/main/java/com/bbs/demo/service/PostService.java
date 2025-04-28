package com.bbs.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bbs.demo.model.Post;

public interface PostService {
    List<Post> getAllPosts();                 // 게시글 목록 조회
    Post getPostById(int post_id);           // 게시글 상세 조회
    void updatePost(Post post, int currentUserId);               // 게시글 수정
    void deletePost(int post_id, int currentUserId);            // 게시글 삭제
    void incrementViewCount(int post_id, int currentUserId);    // 조회수 증가
    void createPost(Post post, int currentUserId,
    		MultipartFile[] images) throws IOException; // 게시글 등록
}
