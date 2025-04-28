package com.bbs.demo.controller;

import java.util.Collections;
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
public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") int postId) {
List<Comment> comments = commentService.getCommentsByPostId(postId);
return ResponseEntity.ok(comments);
}

@PostMapping
public ResponseEntity<Map<String, Boolean>> createComment(@RequestBody Comment comment, HttpSession session) {
        // 세션에서 "userId"로 통일 (중요)
        Integer userId = (Integer) session.getAttribute("userId");
if (userId == null) {
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 안한 경우
}
comment.setUser_id(userId);
commentService.createComment(comment);
return ResponseEntity.ok(Collections.singletonMap("created", true));
}

@PutMapping("/{id}")
public ResponseEntity<Map<String, Boolean>> updateComment(@PathVariable("id") int id, @RequestBody Comment comment, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
if (userId == null) {
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}
comment.setComment_id(id);
boolean result = commentService.updateComment(comment, userId);
if (result) {
return ResponseEntity.ok(Collections.singletonMap("updated", true));
} else {
return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}
}

@DeleteMapping("/{id}")
public ResponseEntity<Map<String, Boolean>> deleteComment(@PathVariable("id") int id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
if (userId == null) {
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}
boolean result = commentService.deleteComment(id, userId);
if (result) {
return ResponseEntity.ok(Collections.singletonMap("deleted", true));
} else {
return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}
}

@PostMapping("/{id}/like")
public ResponseEntity<Map<String, Boolean>> likeComment(@PathVariable("id") int id) {
boolean result = commentService.likeComment(id);
if (result) {
return ResponseEntity.ok(Collections.singletonMap("liked", true));
} else {
return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
}
}

@PostMapping("/{id}/unlike")
public ResponseEntity<Map<String, Boolean>> unlikeComment(@PathVariable("id") int id) {
boolean result = commentService.unlikeComment(id);
if (result) {
return ResponseEntity.ok(Collections.singletonMap("unliked", true));
} else {
return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
}
}
}