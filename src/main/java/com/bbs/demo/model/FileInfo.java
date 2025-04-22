package com.bbs.demo.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FileInfo {
   
   private int file_id;
   private int post_id;
   private String file_origin_name;
   private String file_store_name;
   private String file_path;
   private Long fileSize;
   private LocalDateTime uploadedAt = LocalDateTime.now();
   private Boolean isThumbnail = false;
   private String fileType;
}