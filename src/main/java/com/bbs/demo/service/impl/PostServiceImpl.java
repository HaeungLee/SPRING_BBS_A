package com.bbs.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbs.demo.mapper.PostMapper;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostMapper postMapper;
	
	@Override
	public List<Post> getAllPosts() {
		return postMapper.getAllPosts();
	}

	@Override
	public Post getPostById(int posts_id) {
		return postMapper.getPostById(posts_id);
	}

	@Override
	public void insertPost(Post post) {
		postMapper.insertPost(post);
		
	}

	@Override
	public void updatePost(Post post) {
		postMapper.updatePost(post);
		
	}

	@Override
	public void deletePost(int posts_id) {
		postMapper.deletePost(posts_id);		
	}
    
}
