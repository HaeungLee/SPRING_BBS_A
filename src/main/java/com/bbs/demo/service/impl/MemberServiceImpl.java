package com.bbs.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbs.demo.mapper.MemberMapper;
import com.bbs.demo.model.Member;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Override
    public List<Member> getAllMembers() {
        return memberMapper.getAllMembers();
    }
    
    @Override
    public Member getMemberById(int userId) {
        return memberMapper.getMemberById(userId);
    }
    
    @Override
    public void insertMember(Post post, Member member) {
        member.setIsManager("N");
        memberMapper.insertMember(member);
    }
    
    @Override
    public void updateMember(Post post, Member member) {
        memberMapper.updateMember(member);
    }
    
    @Override
    public void deleteMember(int userId) {
        memberMapper.deleteMember(userId);
    }
    
    @Override
    public Member login(String username, String password) {
        Member member = memberMapper.findByUsername(username);
        if (member != null && password.equals(member.getPassword())) {
            return member;
        }
        return null;
    }
}



