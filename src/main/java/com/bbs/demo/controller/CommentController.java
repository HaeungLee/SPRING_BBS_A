package com.bbs.demo.controller;

import java.util.Collections;
import java.util.List;

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
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") int postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        comment.setUser_id(userId);
        commentService.createComment(comment);
        return ResponseEntity.ok(Collections.singletonMap("created", true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") int id, @RequestBody Comment comment, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        comment.setComment_id(id);
        boolean result = commentService.updateComment(comment, userId);
        return result ? ResponseEntity.ok(Collections.singletonMap("updated", true))
                      : ResponseEntity.status(403).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") int id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        boolean result = commentService.deleteComment(id, userId);
        return result ? ResponseEntity.ok(Collections.singletonMap("deleted", true))
                      : ResponseEntity.status(403).build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("commentId") int commentId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();

        commentService.toggleLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}