package com.bbs.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bbs.demo.model.Post;
import com.bbs.demo.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    /** 게시글 목록 조회 */
    @GetMapping("/list")
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "post/list";      // src/main/resources/templates/post/list.html
    }

    /** 게시글 상세 조회 + 조회수 증가 */
    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable("id") int posts_id, Model model, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if(currentUserId == null) currentUserId = 0;
        
        postService.incrementViewCount(posts_id, currentUserId); // 조회수 증가
        Post post = postService.getPostById(posts_id);
        model.addAttribute("post", post);
        return "post/view";      // src/main/resources/templates/post/view.html
    }

    /** 게시글 등록 폼 */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/form";      // src/main/resources/templates/post/form.html
    }

    /** 게시글 등록 처리 */
    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        postService.createPost(post, currentUserId);
        return "redirect:/post/list";
    }

    /** 게시글 수정 폼 */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int posts_id, Model model, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Post post = postService.getPostById(posts_id);
        if (post == null || post.getUsers_id() != currentUserId) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        model.addAttribute("post", post);
        return "post/form";      // src/main/resources/templates/post/form.html
    }

    /** 게시글 수정 처리 */
    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable("id") int posts_id, @ModelAttribute Post post, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        post.setPosts_id(posts_id);
        postService.updatePost(post, currentUserId);
        return "redirect:/post/view/" + posts_id;
    }

    /** 게시글 삭제 처리 (JS confirm 으로 호출) */
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int posts_id, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        postService.deletePost(posts_id, currentUserId);
        return "redirect:/post/list";
    }
}
