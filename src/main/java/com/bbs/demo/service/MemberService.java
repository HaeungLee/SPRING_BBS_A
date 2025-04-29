package com.bbs.demo.service;

import java.util.List;
import com.bbs.demo.model.Member;
import com.bbs.demo.model.Post;

public interface MemberService {
    
    List<Member> getAllMembers();
    Member getMemberById(int userId);
    void insertMember(Post post, Member member);
    void updateMember(Post post, Member member);
    void deleteMember(int userId);
    Member login(String username, String password);
    // 회원가입
    boolean register(Member member);
    // 아이디 중복 체크
    boolean isUsernameDuplicated(String username);
    Member getMemberByUsername(String username);
}
