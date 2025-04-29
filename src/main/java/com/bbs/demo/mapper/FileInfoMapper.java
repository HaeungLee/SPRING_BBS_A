package com.bbs.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bbs.demo.model.FileInfo;

@Mapper
public interface FileInfoMapper {
	void insertFileInfos(@Param("list") List<FileInfo> files);

	List<FileInfo> findByPostId(@Param("postId") int postId);
	FileInfo findById(@Param("fileId") int fileId);
	void insert(FileInfo file);
	List<FileInfo> findAll();
	List<FileInfo> findFilesByPostId(int postId);
	FileInfo findByStoredFileName(String storedFileName);

	
}