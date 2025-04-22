package com.bbs.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbs.demo.mapper.PostMapper;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Post> getAllPosts() {
        return postMapper.getAllPosts();
    }

    // ✅ 조회수 증가 + 게시글 조회 → 트랜잭션 처리
    @Override
    @Transactional
    public Post getPostById(int posts_id) {
        postMapper.incrementViewCount(posts_id); // 조회수 증가
        return postMapper.getPostById(posts_id); // 게시글 조회
    }

    @Override
    public void createPost(Post post, int currentUserId) {
        if(currentUserId == 0) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        post.setUsers_id(currentUserId);
        LocalDateTime now = LocalDateTime.now();
        post.setCreated_at(now);
        post.setUpdated_at(now);
        post.setViews(0); // 기본값 설정
        postMapper.insertPost(post);
    }

    @Override
    public void updatePost(Post post, int currentUserId) {
        Post existing = postMapper.getPostById(post.getPosts_id());
        if (existing == null || existing.getUsers_id() != currentUserId) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }
        post.setUpdated_at(LocalDateTime.now());
        postMapper.updatePost(post);
    }

    // ✅ 삭제에도 트랜잭션 (댓글 등 관련 처리 확장 대비)
    @Override
    @Transactional
    public void deletePost(int posts_id, int currentUserId) {
        Post post = postMapper.getPostById(posts_id);
        if(post == null || post.getUsers_id() != currentUserId) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        // 향후 commentMapper.deleteByPostId(posts_id); 등 추가 가능
        postMapper.deletePost(posts_id);
    }

    // ⚠️ 기존 코드에서 중복으로 조회수 증가 호출 → 제거
    @Override
    public void incrementViewCount(int posts_id, int currentUserId) {
        Post post = postMapper.getPostById(posts_id);
        if(post != null && post.getUsers_id() != currentUserId) {
            postMapper.incrementViewCount(posts_id);
        }
        // 중복 호출 제거됨
        // postMapper.incrementViewCount(posts_id);
    }
}
