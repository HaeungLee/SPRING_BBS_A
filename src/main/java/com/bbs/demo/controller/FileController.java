package com.bbs.demo.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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

@Controller
@RequestMapping("/post")
public class FileController {

	@Autowired
	private FileInfoMapper filemapper;
	private Path uploadDir = Paths.get("uploads");

	public FileController() throws IOException {
		super();

		Files.createDirectories(uploadDir);
	}

	@GetMapping("/post/form")
	public String showPostForm(Model model) {
		if (!model.containsAttribute("post")) {
			model.addAttribute("post", new Post()); // 빈 객체라도 넣어줘야 함
		}
		return "post/form";
	}

	// 게시글 작성 시 파일 업로드 처리
	@PostMapping("/submit")
	@ResponseBody
	public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

		// 원본 파일명 및 확장자 추출
		String originalName = file.getOriginalFilename();
		String ext = StringUtils.getFilenameExtension(originalName);
		String uuidFile = UUID.randomUUID() + "." + ext;

		// 파일 저장 경로 생성
		Path savePath = uploadDir.resolve(uuidFile);

		// 파일 저장
		Files.copy(file.getInputStream(), savePath);

		// 파일 메타데이터 생성 및 DB 저장
		FileInfo newFile = new FileInfo();
		newFile.setFile_origin_name(originalName); // 원본 파일명
		newFile.setFile_store_name(uuidFile); // 저장 파일명 (UUID)
		newFile.setFile_path(savePath.toString()); // 실제 저장 경로
		newFile.setFileType(file.getContentType()); // MIME 타입
		newFile.setFileSize(file.getSize()); // 파일 크기 (bytes)
		filemapper.insert(newFile);

		// DB에 저장된 파일 정보 조회
		FileInfo savedFile = filemapper.findByStoredFileName(newFile.getFile_store_name());

		// 업로드 결과 JSON 형태로 응답
		return Map.of("id", savedFile.getFile_id(), "originalFileName", savedFile.getFile_origin_name(), "size",
				savedFile.getFileSize(), "uploadDate",
				savedFile.getUploadedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
}
