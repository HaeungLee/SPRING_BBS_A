package com.bbs.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.demo.model.FileInfo;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.PostService;
import com.bbs.demo.service.FileService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@InitBinder("post")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("files");
	}

	/** 게시글 목록 조회 */
	@GetMapping("/list")
	public String listPosts(Model model) {
		List<Post> posts = postService.getAllPosts();

		// 썸네일 정보 채워넣기
		for (Post post : posts) {
			List<FileInfo> files = fileService.getFilesByPostId(post.getPost_id());
			if (files != null && !files.isEmpty()) {
				// 첫 번째 파일을 썸네일로 설정
				FileInfo thumbnailFile = files.get(0);
				post.setThumbnailPath("/post/file/preview/" + thumbnailFile.getFileId());
			}
		}

		model.addAttribute("posts", posts);
		return "post/list";
	}

	/** 게시글 상세 조회 + 조회수 증가 (중요 수정) */
	@GetMapping("/view/{id}")
	public String viewPost(@PathVariable("id") int post_id, Model model, HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		if (currentUserId == null)
			currentUserId = 0;

		// 조회수 증가 + 게시글 조회 (작성자가 아닐 때만 증가)
		Post post = postService.getPostWithViewCount(post_id, currentUserId);
		model.addAttribute("post", post);

		// ✅ 파일 리스트 가져오기
		List<FileInfo> fileList = fileService.getFilesByPostId(post_id);
		model.addAttribute("fileList", fileList);

		return "post/view";
	}

	/** 게시글 등록 폼 */
	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("post", new Post());
		return "post/form";
	}

	/** 게시글 등록 처리 (중요 수정) */
	@PostMapping("/create")
	public String createPost(@ModelAttribute Post post,
			@RequestParam(value = "files", required = false) List<MultipartFile> files, HttpSession session)
			throws IOException {

		Integer currentUserId = (Integer) session.getAttribute("userId");
		if (currentUserId == null) {
			throw new RuntimeException("로그인이 필요합니다.");
		}

		// 1. 위도/경도 값 검증
		if (post.getLat() == null || post.getLng() == null) {
			throw new IllegalArgumentException("위치 정보가 없습니다.");
		}

		// 서비스 계층 호출
		postService.createPost(post, currentUserId, files);

		return "redirect:/post/list";
	}

	/** 게시글 수정 폼 (중요 수정) */
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") int post_id, Model model, HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		if (currentUserId == null) {
			throw new RuntimeException("로그인이 필요합니다.");
		}

		Post post = postService.getPostById(post_id); // 변경
		if (post == null || post.getUser_id() != currentUserId) {
			throw new RuntimeException("수정 권한이 없습니다.");
		}

		model.addAttribute("post", post);
		return "post/form";
	}

	/** 게시글 수정 처리 (중요 수정) */
	@PostMapping("/edit/{id}")
	public String updatePost(@PathVariable("id") int post_id, @ModelAttribute Post post,
			@RequestParam(value = "files", required = false) List<MultipartFile> files, HttpSession session)
			throws IOException {

		Integer currentUserId = (Integer) session.getAttribute("userId");
		if (currentUserId == null) {
			throw new RuntimeException("로그인이 필요합니다.");
		}

		// 1. 위치 정보 검증
		if (post.getLat() == null || post.getLng() == null) {
			throw new IllegalArgumentException("위치 정보가 없습니다.");
		}

		// 2. 파일 처리 (예시)
		if (files != null) {
			// 파일 업로드 및 DB 저장 로직 구현
		}

		// 3. 서비스 계층 호출
		post.setPost_id(post_id);
		postService.updatePost(post, currentUserId);
		return "redirect:/post/view/" + post_id;
	}

	/** 게시글 삭제 처리 */
	@PostMapping("/delete/{id}")
	public String deletePost(@PathVariable("id") int post_id, HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		if (currentUserId == null) {
			throw new RuntimeException("로그인이 필요합니다.");
		}

		postService.deletePost(post_id, currentUserId);
		return "redirect:/post/list";
	}
}