package com.bbs.demo.mapper;

import com.bbs.demo.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    Member findById(Long userId);
    void insert(Member member);
    void update(Member member);
    void delete(Long userId);
}