package com.bbs.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;            // ← 추가
import org.springframework.web.bind.annotation.InitBinder; // ← 추가
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.demo.model.Post;
import com.bbs.demo.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    
    @InitBinder("post")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("files");
    }

    /** 게시글 목록 조회 */
    @GetMapping("/list")
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "post/list";      // src/main/resources/templates/post/list.html
    }

    /** 게시글 상세 조회 + 조회수 증가 */
    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable("id") int post_id,
                           Model model,
                           HttpSession session) {
    	
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) currentUserId = 0;

        Post post = postService.getPostById(post_id);
        if (post == null) {
            return "redirect:/post/list";
        }

        if (!Objects.equals(post.getUser_id(), currentUserId)) {
            postService.incrementViewCount(post_id, currentUserId);
        }

        model.addAttribute("post", post);
        return "post/view";
    }

    /** 게시글 등록 폼 */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/form";      // src/main/resources/templates/post/form.html
    }

    /** 게시글 등록 처리 */
    @PostMapping("/create")
    public String createPost(
            @ModelAttribute Post post,
            @RequestParam(value="files", required=false) List<MultipartFile> files,
            HttpSession session) throws IOException {

        // (1) 세션에서 유저 확인
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // (2) 로그 찍어보기
        System.out.println("userId in session: " + currentUserId);
        System.out.println("업로드할 실제 파일 개수: " + (files == null ? 0 : files.size()));

        // (3) 게시글 저장 (ID 생성)
        postService.createPost(post, currentUserId);

        // (4) files 리스트 처리 (controller 에서 바로 처리해도 되고, service 로 분리해도 됨)
        if (files != null) {
            for (MultipartFile mf : files) {
                if (!mf.isEmpty()) {
                    // 예: 파일 저장 로직
                    String orig = mf.getOriginalFilename();
                    String stored = System.currentTimeMillis() + "_" + orig;
                    mf.transferTo(new java.io.File("upload-dir/" + stored));
                    // 필요 시 DB에 메타데이터 저장
                }
            }
        }

        return "redirect:/post/list";
    }

    /** 게시글 수정 폼 */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int post_id, Model model, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Post post = postService.getPostById(post_id);
        if (post == null || post.getUser_id() != currentUserId) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        model.addAttribute("post", post);
        return "post/form";      // src/main/resources/templates/post/form.html
    }

    /** 게시글 수정 처리 */
    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable("id") int post_id, @ModelAttribute Post post, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        post.setPost_id(post_id);
        postService.updatePost(post, currentUserId);
        return "redirect:/post/view/" + post_id;
    }

    /** 게시글 삭제 처리 (JS confirm 으로 호출) */
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
