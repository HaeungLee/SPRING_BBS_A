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
    
    @Override
    public boolean register(Member member) {
        try {
            // 아이디 중복 체크
            if (isUsernameDuplicated(member.getUsername())) {
                return false;
            }
            
            // 닉네임이 없으면 아이디 = 닉네임
            if (member.getNickname() == null || member.getNickname().trim().isEmpty()) {
                member.setNickname(member.getUsername());
            }
            
            // 관리자 권한 (일반 유저)
            member.setIsManager("N");
            
            // 프로필 이미지가 없으면 기본 이미지 설정 (선택적)
            if (member.getProfileImg() == null || member.getProfileImg().trim().isEmpty()) {
                member.setProfileImg("default-profile.png");
            }
            
            // 회원 등록
            memberMapper.insertMember(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean isUsernameDuplicated(String username) {
        Member existingMember = memberMapper.findByUsername(username);
        return existingMember != null;
    }
}



