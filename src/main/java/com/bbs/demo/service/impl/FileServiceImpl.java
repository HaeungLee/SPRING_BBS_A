package com.bbs.demo.service.impl;

import com.bbs.demo.mapper.FileInfoMapper;
import com.bbs.demo.model.FileInfo;
import com.bbs.demo.service.FileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    private final FileInfoMapper fileInfoMapper;

    @Value("${file.upload.dir}")
    private String uploadDir;

    public FileServiceImpl(FileInfoMapper fileInfoMapper) {
        this.fileInfoMapper = fileInfoMapper;
    }

    // 모든 파일 조회
    public List<FileInfo> getAllFiles() {
        return fileInfoMapper.findAll();
    }

    // ID로 파일 조회
    @Override
    public FileInfo getFileById(int fileId) {
        return fileInfoMapper.findById(fileId);
    }

    @Override
    public List<FileInfo> getFilesByPostId(int postId) {
        return fileInfoMapper.findFilesByPostId(postId);
    }

    // 파일 업로드
    @Override
    public List<FileInfo> uploadFiles(int postId, List<MultipartFile> files) throws IOException {
        List<FileInfo> fileInfoList = new ArrayList<>();

        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs(); // 디렉토리 없으면 생성
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalName = file.getOriginalFilename();
                String storedName = UUID.randomUUID() + "_" + originalName;
                String filePath = uploadDir + File.separator + storedName;

                File dest = new File(filePath);
                try {
                    file.transferTo(dest);
                } catch (IOException e) {
                    throw new IOException("파일 저장 중 오류 발생", e);
                }

                FileInfo fileInfo = new FileInfo();
                fileInfo.setPostId(postId);
                fileInfo.setFileOriginName(originalName);
                fileInfo.setFileStoreName(storedName);
                fileInfo.setFilePath(filePath);
                fileInfo.setFileSize(file.getSize());
                fileInfo.setFileType(file.getContentType());

                fileInfoList.add(fileInfo);
            }
        }

        if (!fileInfoList.isEmpty()) {
            fileInfoMapper.insertFileInfos(fileInfoList);
        }

        return fileInfoList;
    }

    // 파일 다운로드용 정보 반환
    @Override
    public FileInfo downloadFile(int fileId) {
        return fileInfoMapper.findById(fileId);
    }

    // 이미지 파일 미리보기 (byte 배열 반환)
    @Override
    public byte[] previewFile(int fileId) throws IOException {
        FileInfo fileInfo = fileInfoMapper.findById(fileId);
        Path path = Paths.get(fileInfo.getFilePath());
        return Files.readAllBytes(path);
    }
}
