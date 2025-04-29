package com.bbs.demo.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
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

import com.bbs.demo.model.FileInfo;
import com.bbs.demo.service.FileService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    // 파일 미리보기
    @GetMapping("/preview/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> previewFile(@PathVariable("fileId") int fileId) throws IOException {
        try {
            FileInfo fileInfo = fileService.getFileById(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageData = fileService.previewFile(fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, fileInfo.getFileType())
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 파일 다운로드
    @GetMapping("/download/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") int fileId) throws IOException {
        try {
            FileInfo fileInfo = fileService.getFileById(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(fileInfo.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String encodedFileName = URLEncoder.encode(fileInfo.getFileOriginName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileInfo.getFileSize()))
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 파일 업로드 (AJAX 요청용)
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFiles(@RequestParam("postId") int postId,
                                        @RequestParam("files") List<MultipartFile> files) {
        try {
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
            }

            List<FileInfo> uploadedFiles = fileService.uploadFiles(postId, files);
            return ResponseEntity.ok(uploadedFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}