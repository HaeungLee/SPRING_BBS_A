package com.bbs.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bbs.demo.mapper.MemberMapper;
import com.bbs.demo.model.Member;
import com.bbs.demo.model.Post;
import com.bbs.demo.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
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
        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
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

            // 비밀번호 암호화
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            
            // 관리자 권한 (일반 유저)
            member.setIsManager("N");
            
            // 프로필 이미지가 없으면 기본 이미지 설정 (선택적)
            if (member.getProfileImg() == null || member.getProfileImg().trim().isEmpty()) {
                member.setProfileImg("default-profile.png");
            }
            
            // agree_marketing 필드 처리 (Y/N 문자열로 변환 필요할 경우 대비)
            // 현재는 boolean → int/char로 자동 변환되지만, 명시적으로 처리
            
            System.out.println("회원가입 전 데이터 확인: " + member.toString());
            
            // 회원 등록
            memberMapper.insertMember(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("회원가입 실패 원인: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("근본 원인: " + e.getCause().getMessage());
            }
            return false;
        }
    }
    
    @Override
    public boolean isUsernameDuplicated(String username) {
        Member existingMember = memberMapper.findByUsername(username);
        return existingMember != null;
    }
    
    @Override
    public Member getMemberByUsername(String username) {
        return memberMapper.findByUsername(username);
    }
}