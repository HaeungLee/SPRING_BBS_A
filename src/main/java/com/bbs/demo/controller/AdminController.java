package com.bbs.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.demo.model.Post;
import com.bbs.demo.model.Users;
import com.bbs.demo.service.PostService;
import com.bbs.demo.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UsersService usersService;
    private final PostService postService;

    @ModelAttribute("nickname")
    public String getAdminNickname() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/admin/index")
    public String adminHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        model.addAttribute("nickname", nickname);
        return "admin/index";
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        List<Users> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/admin/user-edit/{id}")
    public String editUser(@PathVariable("id") Integer userId, Model model) {
        Users user = usersService.getUserById(userId);
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping("/admin/user-edit/{id}")
    public String updateUser(@PathVariable("id") Integer userId,
                             @ModelAttribute Users user,
                             @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        if (profileImage != null && !profileImage.isEmpty()) {
            String imagePath = saveImage(profileImage);
            user.setProfile_Img(imagePath);
        }

        usersService.updateUser(userId, user);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Integer userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok("success");
    }



    @GetMapping("/admin/posts")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "admin/posts";
    }

    @DeleteMapping("/posts/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deletePost(@PathVariable("id") Long postId, Authentication authentication) {
        String username = authentication.getName();
        Users user = usersService.getUserByUsername(username);
        int currentUserId = user.getUser_Id().intValue();

        postService.deletePost(postId.intValue(), currentUserId, true); // 관리자 삭제이므로 true
        return ResponseEntity.ok("삭제 성공");
    }


    private String saveImage(MultipartFile file) throws IOException {
        String uploadDir = "src/main/resources/static/images/profile/";
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        Files.copy(file.getInputStream(), filePath);
        return "/images/profile/" + fileName;
    }




    /*
    // 전체 댓글 조회
    @GetMapping("/admin/comments")
    public String getAllComments(Model model) {
        List<Comment> comments = commentService.getAllComments();
        model.addAttribute("comments", comments);
        return "admin/comment-list";
    }

    // 특정 댓글 삭제
    @GetMapping("/admin/comments/delete/{id}")
    public String deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/admin/comments";
    }

    // 파일 관리 페이지 -- 임포트필요
    @GetMapping("/admin/files")
    public String manageFiles(Model model) {
        List<File> files = fileService.getAllFiles();
        model.addAttribute("files", files);
        return "admin/file-management";
    }

    // 게시글과 연결되지 않은 파일 정리 -- 임포트필요
    @GetMapping("/admin/files/cleanup")
    public String cleanupFiles() {
        fileService.cleanupUnlinkedFiles();
        return "redirect:/admin/files";
    }
    */
}
