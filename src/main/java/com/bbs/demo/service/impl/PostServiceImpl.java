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

    /** 게시글 목록 조회 */
    @Override
    public List<Post> getAllPosts() {
        return postMapper.getAllPosts();
    }

    /** 게시글 조회 (트랜잭션 처리) */
    @Override
    @Transactional
    public Post getPostById(int post_id) {
        return postMapper.getPostById(post_id); // 게시글 조회
    }

    /** 게시글 조회 + 조회수 증가 (트랜잭션 처리) */
    @Override
    @Transactional
    public Post getPostWithViewCount(int post_id, int currentUserId) {
        incrementViewCount(post_id, currentUserId); // 조회수 증가
        return postMapper.getPostById(post_id);     // 게시글 조회
    }

    /** 게시글 등록 */
    @Override
    public void createPost(Post post, int currentUserId) {
        if (currentUserId == 0) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        post.setUser_id(currentUserId); // 현재 사용자 ID 설정
        LocalDateTime now = LocalDateTime.now();
        post.setCreated_at(now);        // 생성 시간 설정
        post.setUpdated_at(now);        // 수정 시간 설정
        post.setViews(0);               // 기본 조회수 설정
        postMapper.insertPost(post);    // 게시글 DB에 삽입
    }

    /** 게시글 수정 */
    @Override
    public void updatePost(Post post, int currentUserId, boolean isAdmin) {
        Post existing = postMapper.getPostById(post.getPost_id());

        if (existing == null || (!isAdmin && existing.getUser_id() != currentUserId)) {
            throw new RuntimeException("작성자 또는 관리자만 수정할 수 있습니다.");
        }

        post.setUser_id(existing.getUser_id()); // 원래 작성자 ID 설정
        post.setUpdated_at(LocalDateTime.now()); // 수정 시간 갱신
        postMapper.updatePost(post);            // 게시글 DB 업데이트
    }

    /** 게시글 삭제 */
    @Override
    @Transactional
    public void deletePost(int post_id, int currentUserId, boolean isAdmin) {
        Post post = postMapper.getPostById(post_id);

        if (post == null || (!isAdmin && post.getUser_id() != currentUserId)) {
            throw new RuntimeException("작성자 또는 관리자만 삭제할 수 있습니다.");
        }

        // 향후 댓글 삭제 로직 등을 추가 가능 (예: commentMapper.deleteByPostId(post_id);)
        postMapper.deletePost(post_id); // 게시글 DB 삭제
    }
   
    /** 조회수 증가 */
    @Override
    public void incrementViewCount(int post_id, int currentUserId) {
        Post post = postMapper.getPostById(post_id);
        if (post != null && post.getUser_id() != currentUserId) {
            postMapper.incrementViewCount(post_id); // 다른 사용자가 조회하면 조회수 증가
        }
    }
}
