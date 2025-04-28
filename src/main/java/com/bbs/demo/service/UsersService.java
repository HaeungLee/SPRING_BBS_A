package com.bbs.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbs.demo.model.Users;
import com.bbs.demo.repository.UsersRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    // 사용자 ID로 조회
    @Transactional
    public Users getUserById(Long userId) {
        return usersRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
    }
    
    // 전체 회원 조회
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
    // 유저 네임으로 조회
    public Users getUserByUsername(String username) {
        return usersRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Username: " + username));
    }

    // 사용자 정보 수정
    public void updateUser(Long userId, Users updatedUser) {
        Users user = getUserById(userId);  // 사용자 정보 가져오기
        user = Users.builder()
                    .user_id(user.getUser_id())
                    .username(updatedUser.getUsername())
                    .email(updatedUser.getEmail())
                    .nickname(updatedUser.getNickname())
                    .profile_img(updatedUser.getProfile_img())
                    .isManager(updatedUser.getIsManager())
                    .build();
        usersRepository.save(user);
    }
    // Encoding 
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        String sql = "UPDATE Users SET password = ? WHERE username = ?";
        jdbcTemplate.update(sql, "$2a$10$oSMpNzPABGk0nWqzr1OVmegfEwuiwZDQK1O9OExxNRkZ8sgRLAXkO", "junseo");
    }

    // 사용자 삭제
    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }
}
