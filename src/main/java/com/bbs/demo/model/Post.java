package com.bbs.demo.model;

import java.time.LocalDateTime;


public class Post {
	
	private Integer posts_id;
	private String file;
	private String content;
	private String region;
	private Integer views;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private Integer Users_id;
	private boolean isNotice;
	
	
	public Integer getPosts_id() {
		return posts_id;
	}
	public void setPosts_id(Integer posts_id) {
		this.posts_id = posts_id;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	public Integer getUsers_id() {
		return Users_id;
	}
	public void setUsers_id(Integer users_id) {
		Users_id = users_id;
	}
	public boolean isNotice() {
		return isNotice;
	}
	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}
	
	

}
