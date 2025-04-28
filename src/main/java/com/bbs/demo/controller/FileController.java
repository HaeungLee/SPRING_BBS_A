package com.bbs.demo.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.demo.mapper.FileInfoMapper;
import com.bbs.demo.model.FileInfo;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.FileService;
import com.bbs.demo.service.PostService;

@Controller
@RequestMapping("/post")
public class FileController {

	@Autowired
	private PostService postService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadPost(@RequestParam("title") String title, @RequestParam("content") String content,
			@RequestParam("userId") int userId,
			@RequestParam(value = "images", required = false) List<MultipartFile> files) throws IOException {
		
		Post post = new Post();
		post.setTitle(title);
		post.setContent(content);

		postService.createPost(post, userId, files);


		return ResponseEntity.ok().body("파일 포함 게시글 등록 성공");
	}
}