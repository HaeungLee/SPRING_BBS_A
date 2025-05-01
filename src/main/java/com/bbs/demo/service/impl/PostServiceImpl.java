package com.bbs.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbs.demo.mapper.FileInfoMapper;
import com.bbs.demo.mapper.PostMapper;
import com.bbs.demo.model.FileInfo;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private FileInfoMapper fileInfoMapper;

	@Override
	public List<Post> getPosts(int offset, int limit) {
		// 1. 게시글 목록 조회
		List<Post> posts = postMapper.findPosts(offset, limit);

		// 게시글이 없으면 바로 반환
		if (posts == null || posts.isEmpty()) {
			return posts;
		}

		// 2. 게시글 ID 목록 추출
		List<Integer> postIds = posts.stream().map(Post::getPost_id).collect(Collectors.toList());

		// 3. 게시글 ID 목록으로 모든 썸네일을 한 번에 조회
		List<FileInfo> thumbnails = fileInfoMapper.findThumbnailsByPostIds(postIds);

		// 4. 썸네일 정보를 게시글 객체에 매핑
		Map<Integer, Integer> postIdToThumbnailId = new HashMap<>();
		for (FileInfo thumbnail : thumbnails) {
			postIdToThumbnailId.put(thumbnail.getPostId(), thumbnail.getFileId());
		}

		// 5. 각 게시글에 썸네일 ID 설정
		for (Post post : posts) {
		    String html = post.getContent(); // content는 HTML 문자열
		    if (html != null) {
		        Document doc = Jsoup.parse(html);
		        Element firstImg = doc.selectFirst("img");
		        if (firstImg != null) {
		            String src = firstImg.attr("src");
		            post.setThumbnailUrl(src); // 썸네일 URL 설정
		        }
		    }
		}
		return posts;
	}

	@Override
	public List<Post> searchPosts(String type, String keyword, int offset, int limit) {
		// 1. 검색 결과 조회
		List<Post> posts = postMapper.searchPosts(type, keyword, offset, limit);

		// 게시글이 없으면 바로 반환
		if (posts == null || posts.isEmpty()) {
			return posts;
		}

		// 2. 게시글 ID 목록 추출
		List<Integer> postIds = posts.stream().map(Post::getPost_id).collect(Collectors.toList());

		// 3. 게시글 ID 목록으로 모든 썸네일을 한 번에 조회
		List<FileInfo> thumbnails = fileInfoMapper.findThumbnailsByPostIds(postIds);

		// 4. 썸네일 정보를 게시글 객체에 매핑
		Map<Integer, Integer> postIdToThumbnailId = new HashMap<>();
		for (FileInfo thumbnail : thumbnails) {
			postIdToThumbnailId.put(thumbnail.getPostId(), thumbnail.getFileId());
		}

		// 5. 각 게시글에 썸네일 ID 설정
		for (Post post : posts) {
			Integer thumbnailId = postIdToThumbnailId.get(post.getPost_id());
			post.setThumbnailId(thumbnailId);
		}

		return posts;
	}

	@Override
	public List<String> suggestTitles(String keyword) {
		return postMapper.suggestTitles(keyword);
	}

	// ✅ 조회수 증가 + 게시글 조회 → 트랜잭션 처리
	@Override
	@Transactional
	public Post getPostById(int post_id) {
		return postMapper.getPostById(post_id); // 게시글 조회
	}

	@Override
	@Transactional
	public Post getPostWithViewCount(int post_id, int currentUserId) {
		incrementViewCount(post_id, currentUserId);
		return postMapper.getPostById(post_id);
	}

	@Override
	public void createPost(Post post, int currentUserId) {
		if (currentUserId == 0) {
			throw new RuntimeException("로그인이 필요합니다.");
		}
		post.setUser_id(currentUserId);
		LocalDateTime now = LocalDateTime.now();
		post.setCreated_at(now);
		post.setUpdated_at(now);
		post.setViews(0); // 기본값 설정
		postMapper.insertPost(post);
	}

	@Override
	public void updatePost(Post post, int currentUserId) {
		Post existing = postMapper.getPostById(post.getPost_id());
		if (existing == null || existing.getUser_id() != currentUserId) {
			throw new RuntimeException("작성자만 수정할 수 있습니다.");
		}

		// ✅ user_id를 기존 값으로 강제 설정 (폼에서 전달되지 않아도 됨)
		post.setUser_id(existing.getUser_id());
		post.setUpdated_at(LocalDateTime.now());
		postMapper.updatePost(post);
	}

	// ✅ 삭제에도 트랜잭션 (댓글 등 관련 처리 확장 대비)
	@Override
	@Transactional
	public void deletePost(int post_id, int currentUserId) {
		Post post = postMapper.getPostById(post_id);

		// 관리자(currentUserId=0)는 모든 게시글 삭제 가능
		if (currentUserId == 0) {
			// 향후 commentMapper.deleteByPostId(post_id); 등 추가 가능
			postMapper.deletePost(post_id);
			return;
		}

		// 일반 사용자는 자신의 게시글만 삭제 가능
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
