package com.bbs.demo.service.impl;

import com.bbs.demo.mapper.FileInfoMapper;
import com.bbs.demo.model.FileInfo;
import com.bbs.demo.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
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

	//
	@Override
	public List<FileInfo> getFilesByPostId(int postId) {
		return fileInfoMapper.findFilesByPostId(postId);
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
	public List<FileInfo> uploadFiles(int postId, List<MultipartFile> files) throws IOException {
	    List<FileInfo> fileInfoList = new ArrayList<>();

	    System.out.println("[uploadFiles] 호출됨");
	    System.out.println("[uploadFiles] postId = " + postId);

	    File uploadDirectory = new File(uploadDir);
	    if (!uploadDirectory.exists()) {
	        uploadDirectory.mkdirs(); // 디렉토리 없으면 생성
	    }

	    for (MultipartFile file : files) {
	        if (!file.isEmpty()) {
	            System.out.println("[uploadFiles] 파일 이름 = " + file.getOriginalFilename());

	            String originalName = file.getOriginalFilename();
	            String storedName = UUID.randomUUID() + "_" + originalName;
	            String filePath = Paths.get(uploadDir, storedName).toString();

	            File dest = new File(filePath);
	            file.transferTo(dest);

	            FileInfo fileInfo = new FileInfo();
	            fileInfo.setPost_id(postId);
	            fileInfo.setFile_origin_name(originalName);
	            fileInfo.setFile_store_name(storedName);
	            fileInfo.setFile_path(filePath);
	            fileInfo.setFileSize(file.getSize());
	            fileInfo.setFileType(file.getContentType());

	            fileInfoList.add(fileInfo);
	        }
	    }

	    System.out.println("[uploadFiles] 저장할 파일 수 = " + fileInfoList.size());

	    if (!fileInfoList.isEmpty()) {
	        System.out.println("[uploadFiles] DB에 insert 시작!");
	        fileInfoMapper.insertFileInfos(fileInfoList);
	    } else {
	        System.out.println("[uploadFiles] 저장할 파일 없음. insert 안 함.");
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
		Path path = Paths.get(fileInfo.getFile_path());
		return Files.readAllBytes(path);
	}

}
