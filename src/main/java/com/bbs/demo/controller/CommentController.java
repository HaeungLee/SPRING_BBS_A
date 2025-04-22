package com.bbs.demo.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbs.demo.model.Comment;
import com.bbs.demo.model.Member;
import com.bbs.demo.service.CommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping("/{postId}")
	public List<Comment> getComments(@PathVariable int postId) {
		return commentService.getCommentsByPostId(postId);

	}

	public ResponseEntity<?> createComment(@RequestBody Comment comment, HttpSession session) {

		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		comment.setUsers_id(loginUser.getUser_id());
		commentService.createComment(comment);
		return ResponseEntity.ok().build();

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateComment(@PathVariable int id, @RequestBody Map<String, String> body,
			HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Comment comment = new Comment();
		comment.setComment_id(id);
		comment.setContent(body.get("content"));
		boolean result = commentService.updateComment(comment, loginUser.getUser_id());
		return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable int id, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		boolean result = commentService.deleteComment(id, loginUser.getUser_id());
		return result ? ResponseEntity.ok(Collections.singletonMap("deleted", true))
				: ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

}
