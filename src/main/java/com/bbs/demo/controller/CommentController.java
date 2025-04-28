package com.bbs.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bbs.demo.model.Comment;
import com.bbs.demo.service.CommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<?> getComments(@PathVariable("postId") int postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "댓글 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment, HttpSession session) {
        // 세션에서 "userId"로 통일 (중요)
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            comment.setUser_id(userId);
            commentService.createComment(comment);
            return ResponseEntity.ok(Collections.singletonMap("created", true));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "댓글 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") int id, @RequestBody Comment comment, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            comment.setComment_id(id);
            boolean result = commentService.updateComment(comment, userId);
            if (result) {
                return ResponseEntity.ok(Collections.singletonMap("updated", true));
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "댓글 수정 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "댓글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") int id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            boolean result = commentService.deleteComment(id, userId);
            if (result) {
                return ResponseEntity.ok(Collections.singletonMap("deleted", true));
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "댓글 삭제 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeComment(@PathVariable("id") int id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            boolean result = commentService.likeComment(id);
            if (result) {
                return ResponseEntity.ok(Collections.singletonMap("liked", true));
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "댓글을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "댓글 좋아요 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<?> unlikeComment(@PathVariable("id") int id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            boolean result = commentService.unlikeComment(id);
            if (result) {
                return ResponseEntity.ok(Collections.singletonMap("unliked", true));
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "댓글을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "댓글 좋아요 취소 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}