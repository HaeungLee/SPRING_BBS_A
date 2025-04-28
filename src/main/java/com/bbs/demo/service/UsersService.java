package com.bbs.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbs.demo.model.Users;
import com.bbs.demo.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    
    @Lazy // 순환때문에 적엇습니다
    private final PasswordEncoder passwordEncoder;

	private String username;

	private String email;
    
    @Autowired
    public UsersService(PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }
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
/*
    // 사용자 정보 수정
    public void updateUser(Long userId, Users updatedUser) {
        Users user = getUserById(userId);  // 사용자 정보 가져오기
        user = Users.builder()
                    .user_Id(user.getUser_Id())
                    .username(updatedUser.getUsername())
                    .email(updatedUser.getEmail())
                    .nickname(updatedUser.getNickname())
                    .profile_Img(updatedUser.getProfile_Img())
                    .is_Manager(updatedUser.getIs_Manager())
                    .build();
        usersRepository.save(user);
    }*/
    
    
    
 // UsersService 수정
    public void updateUser(Long userId, Users updatedUser) {
        Users user = getUserById(userId);
        user.updateFields(updatedUser);  // 엔티티 내부에서 null 체크 후 필드 업데이트
        usersRepository.save(user);
    }

    // Users 엔티티에 추가
    public void updateFields(Users updatedUser) {
        if (updatedUser.getUsername() != null) this.username = updatedUser.getUsername();
        if (updatedUser.getEmail() != null) this.email = updatedUser.getEmail();
        
        
    }

    // 사용자 비밀번호 인코딩 후 저장
    public void encodeAndSavePassword(Long userId, String rawPassword) {
        Users user = getUserById(userId); // 사용자를 DB에서 가져옴
        String encodedPassword = passwordEncoder.encode(rawPassword);  // 비밀번호 인코딩
        user.setPassword(encodedPassword);  // 인코딩된 비밀번호를 설정
        usersRepository.save(user);  // DB에 사용자 정보 저장
    }

    // 사용자 비밀번호 확인 (로그인 시 사용)
    public boolean checkPassword(Long userId, String rawPassword) {
        Users user = getUserById(userId);  // 사용자 정보 가져오기
        String encodedPassword = user.getPassword();  // DB에서 가져온 인코딩된 비밀번호

        return passwordEncoder.matches(rawPassword, encodedPassword);  // 입력된 비밀번호와 비교
    }
    // 사용자 삭제
    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }
}
