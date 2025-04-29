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

import com.bbs.demo.mapper.FileInfoMapper;
import com.bbs.demo.model.FileInfo;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.FileService;
import com.bbs.demo.service.PostService;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    // 파일 미리보기
    @GetMapping("/preview/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> previewFile(@PathVariable("fileId") int fileId) throws IOException {
        FileInfo fileInfo = fileService.getFileById(fileId);
        Path path = Paths.get(fileInfo.getFilePath());
        byte[] image = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, fileInfo.getFileType())
                .body(image);
    }

    // 파일 다운로드
    @GetMapping("/download/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") int fileId) throws IOException {
        FileInfo fileInfo = fileService.getFileById(fileId);
        Path path = Paths.get(fileInfo.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        String encodedFileName = URLEncoder.encode(fileInfo.getFileOriginName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
    }
}
