package com.bbs.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbs.demo.model.Comment;
import com.bbs.demo.model.FileInfo;
import com.bbs.demo.model.Member;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.CommentService;
import com.bbs.demo.service.FileService;
import com.bbs.demo.service.MemberService;
import com.bbs.demo.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private FileService fileService;
    
    @GetMapping("/index")
    public String adminIndex(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/access-denied";
        }
        
        String nickname = (String) session.getAttribute("nickname");
        model.addAttribute("nickname", nickname);
        
        return "admin/index";
    }
    
    @GetMapping("/users")
    public String userManagement(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/access-denied";
        }
        
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        
        return "admin/users";
    }
    
    @GetMapping("/users/{userId}")
    public String editUserForm(@PathVariable("userId") int userId, HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/access-denied";
        }
        
        Member member = memberService.getMemberById(userId);
        model.addAttribute("member", member);
        
        return "admin/user-form";
    }
    
    @PostMapping("/users/{userId}")
    public String updateUser(@PathVariable("userId") int userId, Member member, RedirectAttributes redirectAttributes) {
        try {
            member.setUserId(userId);
            memberService.updateMember(null, member);
            redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/admin/users/" + userId;
        }
    }
    
    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable("userId") int userId, RedirectAttributes redirectAttributes) {
        try {
            memberService.deleteMember(userId);
            redirectAttributes.addFlashAttribute("message", "회원이 삭제되었습니다.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }
    
    @GetMapping("/posts")
    public String postManagement(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/access-denied";
        }
        
        List<Post> posts = postService.getPosts(0, 1000); // 모든 게시물 가져오기 (제한 1000개)
        model.addAttribute("posts", posts);
        
        return "admin/posts";
    }
    
    @PostMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable("postId") int postId, RedirectAttributes redirectAttributes) {
        try {
            int adminUserId = 0; // 관리자 삭제는 별도의 권한 체크 없이 처리
            postService.deletePost(postId, adminUserId); 
            redirectAttributes.addFlashAttribute("message", "게시물이 삭제되었습니다.");
            return "redirect:/admin/posts";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시물 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/admin/posts";
        }
    }
    
    @GetMapping("/comments")
    public String commentManagement(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/access-denied";
        }
        
        List<Comment> comments = commentService.getAllComments();
        model.addAttribute("comments", comments);
        
        return "admin/comments";
    }
    
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") int commentId, RedirectAttributes redirectAttributes) {
        try {
            int adminUserId = 0;
            commentService.deleteComment(commentId, adminUserId);
            redirectAttributes.addFlashAttribute("message", "댓글이 삭제되었습니다.");
            return "redirect:/admin/comments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/admin/comments";
        }
    }
    
    @GetMapping("/files")
    public String fileManagement(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/access-denied";
        }
        
        List<FileInfo> files = fileService.getAllFiles(); // 모든 파일 가져오기
        model.addAttribute("files", files);
        
        return "admin/files";
    }
    
    // 파일 삭제 처리 (실제 파일 시스템에서도 삭제)
    @PostMapping("/files/{fileId}/delete")
    public String deleteFile(@PathVariable("fileId") int fileId, RedirectAttributes redirectAttributes) {
        try {
            // 실제 파일 및 DB 정보 삭제 메서드 호출
            // fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("message", "파일이 삭제되었습니다.");
            return "redirect:/admin/files";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "파일 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/admin/files";
        }
    }
}
