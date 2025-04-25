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
	

//	// 첨부파일 다운로드
//	@GetMapping("/post/download/{id}") // content-Disposition 설정되어 있으면 파일을 다운로드하도록 지시받음
//	public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) throws IOException {
//		FileInfo file = filemapper.findById(id);
//		Path filePath = uploadDir.resolve(file.getFile_store_name());
//		Resource resource = new UrlResource(filePath.toUri());
//
//		String encodedFileName = URLEncoder.encode(file.getFile_origin_name(), StandardCharsets.UTF_8).replaceAll("\\+","%20");
//
//		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM) // 브라우저가 파일을 다운로드 하도록 강제
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
//				.body(resource);
//	}
	
	/*
	 * @GetMapping("/post/view/{id}") // content_disposition 헤더가 설정되지 않으면 mime타입에 따라
	 * 미리보기 또는 직접보기 모드로 처리 public ResponseEntity<Resource>
	 * viewFile(@PathVariable("id") Long id) throws IOException { FileInfo file =
	 * filemapper.findById(id); Path filePath =
	 * uploadDir.resolve(file.getFile_store_name()); Resource resource = new
	 * UrlResource(filePath.toUri());
	 * 
	 * String mimeType = file.getFileType(); if (mimeType == null ||
	 * mimeType.isEmpty()) { mimeType = "application/octet-stream"; }
	 * 
	 * return ResponseEntity.ok() // 파일을 구분하려고 하는 것. 누르면 다운로드 / 아니면 볼 수 있게
	 * .contentType(MediaType.parseMediaType(file.getFileType())).body(resource); }
	 */

	/*
	 * @GetMapping("/delete/{id}") public String deleteFile(@PathVariable("id") Long
	 * id) throws IOException { FileInfo file = filemapper.findById(id);
	 * 
	 * // 파일 삭제 Path filePath = uploadDir.resolve(file.getFile_store_name());
	 * Files.deleteIfExists(filePath);
	 * 
	 * // DB에서 삭제 filemapper.deleteById(id);
	 * 
	 * // 파일을 업로드한 게시글의 view로 리다이렉트 하는 것이 이상적 // 여기서는 단순 리스트로 이동한다고 가정 return
	 * "redirect:/post/view/"; }
	 */
}
//MIME 타입(Content-Type)이 이미지, PDF등 브라우저에서 기본적으로 지원하는 형식일 경우, 브라우저는 파일을 다운로드 대신 바로 열어버릴 수 있음
//RFC 5987 표준을 준수하면 대부분의 브라우저에서 한글 파일명을 올바르게 처리할 수 있음
