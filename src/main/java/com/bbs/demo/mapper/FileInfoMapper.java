package com.bbs.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bbs.demo.model.FileInfo;

//INSERT 시 post_id 꼭 넣어야 합니다.
//SELECT 시 post_id도 가져와야 합니다.
//파일 조회 시 post_id로 조회하는 기능이 필요합니다.

@Mapper
public interface FileInfoMapper {
	void insertFileInfos(@Param("list") List<FileInfo> files);

	List<FileInfo> findByPostId(@Param("postId") int postId);

	FileInfo findById(@Param("fileId") int fileId);

	void insert(FileInfo file);

	List<FileInfo> findAll();

	FileInfo findByStoredFileName(String storedFileName);
}
