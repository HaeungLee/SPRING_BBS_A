package com.bbs.demo.service;

import java.util.List;
import com.bbs.demo.model.Post;

public interface PostService {

    // 모든 게시글 목록을 조회합니다.
    List<Post> getAllPosts();                 
    
    // 게시글을 조회하면서, 작성자가 아닌 경우 조회수를 1 증가시킵니다.
    Post getPostWithViewCount(int post_id, int currentUserId);  
    
    // 게시글 ID로 게시글 상세 정보를 조회합니다. (조회수 증가 없음)
    Post getPostById(int post_id);           
    
    // 새로운 게시글을 작성합니다. 작성자 ID는 currentUserId로 설정됩니다.
    void createPost(Post post, int currentUserId);               

    // 게시글을 수정합니다. 작성자 본인 또는 관리자만 수정할 수 있습니다.
    void updatePost(Post post, int currentUserId, boolean isAdmin);  

    // 게시글을 삭제합니다. 작성자 본인 또는 관리자만 삭제할 수 있습니다.
    void deletePost(int post_id, int currentUserId, boolean isAdmin); 

    // 게시글의 조회수를 증가시킵니다. 단, 작성자 본인은 증가하지 않음.
    void incrementViewCount(int post_id, int currentUserId);    
}
