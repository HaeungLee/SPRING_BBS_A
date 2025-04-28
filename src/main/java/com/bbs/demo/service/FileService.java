package com.bbs.demo.service;

import com.bbs.demo.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileInfo> getAllFiles(); // 모든 파일 목록
    
    FileInfo getFileById(int fileId);     // 파일 ID로 단일 파일 조회
    
    List<FileInfo> uploadFiles(int postId, List<MultipartFile> files) throws IOException;     // 파일 업로드

    FileInfo downloadFile(int fileId);     // 파일 다운로드 (서버 저장 경로 반환 or 파일 스트림)

    byte[] previewFile(int fileId) throws IOException;     // 이미지 미리보기용 파일 정보
}
