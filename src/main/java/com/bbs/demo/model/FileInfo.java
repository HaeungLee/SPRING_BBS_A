package com.bbs.demo.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FileInfo {
   private int fileId;
   private int postId;
   private String fileOriginName;
   private String fileStoreName;
   private String filePath;
   private Long fileSize;
   private LocalDateTime uploadedAt = LocalDateTime.now();
   private Boolean isThumbnail = false;
   private String fileType;
}