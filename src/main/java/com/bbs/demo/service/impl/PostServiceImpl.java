package com.bbs.demo.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.demo.mapper.PostMapper;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.FileService;
import com.bbs.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostMapper postMapper;

	@Override
	public List<Post> getAllPosts() {
		return postMapper.getAllPosts();
	}

	@Autowired
	private FileService fileService; // FileService 주입

	// ✅ 조회수 증가 + 게시글 조회 → 트랜잭션 처리
	@Override
	@Transactional
	public Post getPostById(int post_id) {
		postMapper.incrementViewCount(post_id); // 조회수 증가
		return postMapper.getPostById(post_id); // 게시글 조회
	}

	// view
	@Override
	@Transactional
	public Post getPostWithViewCount(int post_id, int currentUserId) {
		incrementViewCount(post_id, currentUserId);
		return postMapper.getPostById(post_id);
	}

	// 게시글 등록
	public void createPost(Post post, int currentUserId, List<MultipartFile> files) throws IOException {
		if (currentUserId == 0) {
			throw new RuntimeException("로그인이 필요합니다.");
		}
		post.setUser_id(currentUserId);
		LocalDateTime now = LocalDateTime.now();
		post.setCreated_at(now);
		post.setUpdated_at(now);
		post.setViews(0); // 기본값 설정
		postMapper.insertPost(post);

		// 이미지 존재 여부
		if (files != null && !files.isEmpty()) {
			fileService.uploadFiles(post.getPost_id(), files);
		}
	}

	// 게시글 수정
	@Override
	public void updatePost(Post post, int currentUserId) {
		Post existing = postMapper.getPostById(post.getPost_id());
		if (existing == null || existing.getUser_id() != currentUserId) {
			throw new RuntimeException("작성자만 수정할 수 있습니다.");
		}
		post.setUpdated_at(LocalDateTime.now());
		postMapper.updatePost(post);
	}

	// ✅ 삭제에도 트랜잭션 (댓글 등 관련 처리 확장 대비)
	@Override
	@Transactional
	public void deletePost(int post_id, int currentUserId) {
		Post post = postMapper.getPostById(post_id);
		if (post == null || post.getUser_id() != currentUserId) {
			throw new RuntimeException("작성자만 삭제할 수 있습니다.");
		}

		// 향후 commentMapper.deleteByPostId(post_id); 등 추가 가능
		postMapper.deletePost(post_id);
	}

	// ⚠️ 기존 코드에서 중복으로 조회수 증가 호출 → 제거
	@Override
	public void incrementViewCount(int post_id, int currentUserId) {
		Post post = postMapper.getPostById(post_id);
		if (post != null && post.getUser_id() != currentUserId) {
			postMapper.incrementViewCount(post_id);
		}
	}
}