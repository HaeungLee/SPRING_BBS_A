package com.bbs.demo.service;

import java.util.List;

import com.bbs.demo.model.Post;

public interface PostService {
	   List<Post> getAllPosts();
	    Post getPostById(int posts_id);
	    void insertPost(Post post);
	    void updatePost(Post post);
	    void deletePost(int posts_id);
}
