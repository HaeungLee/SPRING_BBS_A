package com.bbs.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bbs.demo.model.Post;
import com.bbs.demo.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시글 목록 조회
    @GetMapping
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "post/list";  // /src/main/resources/templates/post/list.html
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") int posts_id, Model model) {
        Post post = postService.getPostById(posts_id);
        model.addAttribute("post", post);
        return "post/view";
    }

    // 게시글 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/form";
    }

    // 게시글 등록 처리
    @PostMapping("/new")
    public String createPost(@ModelAttribute Post post) {
        postService.insertPost(post);
        return "redirect:/list";
    }

    // 게시글 수정 폼
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int posts_id, Model model) {
        Post post = postService.getPostById(posts_id);
        model.addAttribute("post", post);
        return "post/form";
    }

    // 게시글 수정 처리
    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable("id") int posts_id, @ModelAttribute Post post) {
        post.setPosts_id(posts_id); // ID 설정
        postService.updatePost(post);
        return "redirect:/list";
    }

    // 게시글 삭제 처리
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int posts_id) {
        postService.deletePost(posts_id);
        return "redirect:/list";
    }
}
