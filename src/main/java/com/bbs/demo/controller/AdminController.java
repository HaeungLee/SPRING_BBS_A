package com.bbs.demo.controller;

import com.bbs.demo.model.Users;
import com.bbs.demo.service.UsersService;
import com.bbs.demo.model.Post;
import com.bbs.demo.model.Comment;
import com.bbs.demo.service.PostService;
import com.bbs.demo.service.CommentService;
import com.bbs.demo.service.FileService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// 파일업로드, comment 등록후 변경예정
@Controller
@RequiredArgsConstructor
public class AdminController {
	 
	// 생성자 주입 - final
    private final UsersService usersService;
    private final PostService postService;
    //private final CommentService commentService;
    //private final FileService fileService;
    
    // 다른 페이지에 닉네임 나오게 하기
    @ModelAttribute("nickname")
    public String getAdminNickname() {  
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();  // 로그인한 사용자의 닉네임을 반환
    }
    @GetMapping("/admin/index")
    // 관리자 홈 페이지로 이동
    public String adminHome(Model model) {
	    // 로그인한 사용자 정보 가져오기
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String nickname = authentication.getName();  // 예: nickname을 가져오는 방식 (DB에서 가져올 수 있음)
	    
	    // 모델에 닉네임 추가
	    model.addAttribute("nickname", nickname);
        return "admin/index";  
    }
    
    // 전체 회원 조회
    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        List<Users> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // 회원 정보 수정 페이지
    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable("id") Long userId, Model model) {
        Users user = usersService.getUserById(userId); // 회원 정보 가져오기
        model.addAttribute("user", user); // 회원 정보 model에 저장
        return "admin/user-edit";
    }

    // 회원 정보 업데이트
    @PostMapping("/admin/users/edit/{id}")
    public String updateUser(@PathVariable("id") Long userId, @ModelAttribute Users user) {
        usersService.updateUser(userId, user); // 수정된 정보 업데이트
        return "redirect:/admin/users";
    }

    // 회원 삭제
    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userId) {
        usersService.deleteUser(userId);
        return "redirect:/admin/users";
    }

    // 전체 게시글 조회
    @GetMapping("/admin/posts")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "admin/posts";
    }
   // 게시글 삭제
    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long postId, Authentication authentication) {
        // 로그인한 사용자의 username으로부터 사용자 정보 가져오기
        String username = authentication.getName();
        Users user = usersService.getUserByUsername(username);  // 사용자 정보 가져오기
        int currentUserId = user.getUser_Id().intValue();       // user_id 추출

        postService.deletePost(postId.intValue(), currentUserId);  // 게시글 삭제 호출
        return "redirect:/admin/posts";  // 게시글 목록 페이지로 리다이렉트
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
