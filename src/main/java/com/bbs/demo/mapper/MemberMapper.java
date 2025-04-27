package com.bbs.demo.mapper;

import com.bbs.demo.model.Member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    List<Member> getAllMembers();
    Member getMemberById(int userId);
    Member findByUsername(String username);
    void insertMember(Member member);
    void updateMember(Member member);
    void deleteMember(int userId);
    int countAll();
    
}