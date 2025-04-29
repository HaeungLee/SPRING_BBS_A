package com.bbs.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.demo.model.FileInfo;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.FileService;
import com.bbs.demo.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private FileService fileService;
    
    @InitBinder("post")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("files");
    }

    /** 게시글 목록 조회 */
    @GetMapping("/list")
    public String postsPage(@RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            Model model) {
        List<Post> posts;

        if (keyword != null && !keyword.isEmpty()) {
            posts = postService.searchPosts(type, keyword, 0, 17);  // 검색어 + 타입
        } else {
            posts = postService.getPosts(0, 17);  // 검색어 없으면 전체 목록
        }

        // 각 게시글의 썸네일 이미지 ID 설정
        for (Post post : posts) {
            FileInfo thumbnail = fileService.getThumbnailByPostId(post.getPost_id());
            if (thumbnail != null) {
                post.setThumbnailId(thumbnail.getFileId());
            }
        }

        model.addAttribute("posts", posts);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "post/list";
    }

    @GetMapping("/list/more")
    public String morePosts(@RequestParam("offset") int offset,
                            @RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            Model model) {
        List<Post> posts;

        if (keyword != null && !keyword.isEmpty()) {
            posts = postService.searchPosts(type, keyword, offset, 10);  // 검색 내용 이어 받기
        } else {
            posts = postService.getPosts(offset, 10);  // 전체 목록 계속
        }

        // 각 게시글의 썸네일 이미지 ID 설정
        for (Post post : posts) {
            FileInfo thumbnail = fileService.getThumbnailByPostId(post.getPost_id());
            if (thumbnail != null) {
                post.setThumbnailId(thumbnail.getFileId());
            }
        }

        model.addAttribute("posts", posts);
        return "post/fragments :: postListFragment";
    }

    @GetMapping("/suggest")
    @ResponseBody
    public List<String> suggestTitles(@RequestParam("keyword") String keyword) {
        return postService.suggestTitles(keyword);
    }
    
    /** 게시글 상세 조회 + 조회수 증가 (중요 수정) */
    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable("id") int post_id, Model model, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) currentUserId = 0;

        // 조회수 증가 + 게시글 조회 (작성자가 아닐 때만 증가)
        Post post = postService.getPostWithViewCount(post_id, currentUserId);
        
        // 게시글에 첨부된 파일 목록 가져오기
        List<FileInfo> files = fileService.getFilesByPostId(post_id);
        post.setFiles(files);
        
        // 메인 이미지(썸네일) 가져오기
        FileInfo mainImage = fileService.getThumbnailByPostId(post_id);
        
        model.addAttribute("post", post);
        model.addAttribute("mainImage", mainImage); // 메인 이미지 모델에 추가
        model.addAttribute("sessionUserId", currentUserId);
        
        Integer userId = (Integer) session.getAttribute("userId"); // 주의! user_id
        if (userId != null) {
            model.addAttribute("user_id", userId); // 뷰에 user_id 넘겨줌
        }
        
        return "post/view";
    }
    
    /** 게시글 등록 폼 */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/form";
    }

    /** 게시글 등록 처리 (중요 수정) */
    @PostMapping("/create")
    public String createPost(
            @ModelAttribute Post post,
            @RequestParam(value="files", required=false) List<MultipartFile> files,
            HttpSession session) throws IOException {

        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // 1. 위도/경도 값 검증
        if (post.getLat() == null || post.getLng() == null) {
            throw new IllegalArgumentException("위치 정보가 없습니다.");
        }

        // 2. 게시글 생성
        postService.createPost(post, currentUserId);
        
        // 3. 게시글이 성공적으로 생성된 후 post_id가 세팅되었는지 확인
        Integer postId = post.getPost_id();
        if (postId != null && files != null && !files.isEmpty()) {
            // 파일 업로드 처리
            fileService.uploadFiles(postId, files);
        }

        return "redirect:/post/list";
    }

    /** 게시글 수정 폼 (중요 수정) */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int post_id, Model model, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Post post = postService.getPostById(post_id); // 변경
        if (post == null || post.getUser_id() != currentUserId) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 게시글에 첨부된 파일 목록 가져오기
        List<FileInfo> files = fileService.getFilesByPostId(post_id);
        post.setFiles(files);

        model.addAttribute("post", post);
        return "post/form";
    }

    /** 게시글 수정 처리 (중요 수정) */
    @PostMapping("/edit/{id}")
    public String updatePost(
            @PathVariable("id") int post_id, 
            @ModelAttribute Post post, 
            @RequestParam(value="files", required=false) List<MultipartFile> files,
            HttpSession session) throws IOException {
        
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // 1. 위치 정보 검증
        if (post.getLat() == null || post.getLng() == null) {
            throw new IllegalArgumentException("위치 정보가 없습니다.");
        }

        // 2. 서비스 계층 호출
        post.setPost_id(post_id);
        postService.updatePost(post, currentUserId);
        
        // 3. 파일 업로드 처리
        if (files != null && !files.isEmpty()) {
            fileService.uploadFiles(post_id, files);
        }

        return "redirect:/post/view/" + post_id;
    }

    /** 게시글 삭제 처리 */
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int post_id, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        postService.deletePost(post_id, currentUserId);
        return "redirect:/post/list";
    }
}
