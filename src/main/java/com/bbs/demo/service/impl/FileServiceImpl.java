package com.bbs.demo.service.impl;

import com.bbs.demo.mapper.FileInfoMapper;
import com.bbs.demo.model.FileInfo;
import com.bbs.demo.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final String uploadDirectory;

    @Autowired
    public FileServiceImpl(FileInfoMapper fileInfoMapper,
                          @Autowired(required = false) String fileUploadDirectory) {
        this.fileInfoMapper = fileInfoMapper;
        this.uploadDirectory = fileUploadDirectory != null ?
                              fileUploadDirectory : System.getProperty("user.home") + "/uploads";
                              
        // 디렉토리가 없으면 생성
        File directory = new File(this.uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // 모든 파일 조회
    @Override
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
        return fileInfoMapper.findByPostId(postId);
    }

    // 썸네일 이미지 가져오기
    @Override
    public FileInfo getThumbnailByPostId(int postId) {
        List<FileInfo> files = fileInfoMapper.findByPostId(postId);
        if (files == null || files.isEmpty()) {
            return null;
        }
        
        // 먼저 isThumbnail이 true인 파일을 찾음
        for (FileInfo file : files) {
            if (file.getIsThumbnail() && file.getFileType() != null && file.getFileType().startsWith("image/")) {
                return file;
            }
        }
        
        // 없으면 첫 번째 이미지 파일을 리턴
        for (FileInfo file : files) {
            if (file.getFileType() != null && file.getFileType().startsWith("image/")) {
                return file;
            }
        }
        
        return null;
    }

    // 파일 업로드
    @Override
    public List<FileInfo> uploadFiles(int postId, List<MultipartFile> files) throws IOException {
        List<FileInfo> fileInfoList = new ArrayList<>();

        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 디렉토리 없으면 생성
        }

        boolean thumbnailSet = false;

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalName = file.getOriginalFilename();
                String storedName = UUID.randomUUID() + "_" + originalName;
                String filePath = uploadDirectory + File.separator + storedName;

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

                // 첫 번째 이미지 파일을 썸네일로 설정
                if (!thumbnailSet && file.getContentType() != null && file.getContentType().startsWith("image/")) {
                    fileInfo.setIsThumbnail(true);
                    thumbnailSet = true;
                } else {
                    fileInfo.setIsThumbnail(false);
                }

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
        if (fileInfo == null) {
            throw new IOException("파일을 찾을 수 없습니다: " + fileId);
        }
        Path path = Paths.get(fileInfo.getFilePath());
        if (!Files.exists(path)) {
            throw new IOException("파일이 존재하지 않습니다: " + fileInfo.getFilePath());
        }
        return Files.readAllBytes(path);
    }
}