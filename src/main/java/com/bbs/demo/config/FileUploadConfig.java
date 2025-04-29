package com.bbs.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    
    @Value("${file.upload.dir}")
    private String uploadDir;
    
    @Value("${file.upload.path:/uploads}")
    private String uploadPath;
    
    @Bean
    public String fileUploadDirectory() {
        // 상대 경로가 지정된 경우 사용자 홈 디렉토리 기준으로 설정
        File directory;
        if (!uploadDir.startsWith("/") && !uploadDir.contains(":")) {
            String userHome = System.getProperty("user.home");
            directory = new File(userHome, uploadDir);
        } else {
            directory = new File(uploadDir);
        }
        
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        return directory.getAbsolutePath();
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + fileUploadDirectory() + File.separator);
    }
}
