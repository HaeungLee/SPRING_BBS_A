package com.bbs.demo.mapper;

import java.util.List;
import java.util.Map;

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

	FileInfo findByStoredFileName(String storedFileName);
	
	void deleteById(@Param("fileId") int fileId);
	
	List<FileInfo> findThumbnailsByPostIds(@Param("postIds") List<Integer> postIds);
}
