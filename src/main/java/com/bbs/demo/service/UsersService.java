package com.bbs.demo.service;

import java.util.List;

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

    private final @Lazy PasswordEncoder passwordEncoder;  // 암호화 처리용 PasswordEncoder
    private final UsersRepository usersRepository;  // 사용자 데이터를 처리하는 레포지토리
    
    // 사용자 등록 시 비밀번호 암호화 후 저장
    @Transactional
    public void registerUser(Users user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());  // 비밀번호 암호화
        user.setPassword(encodedPassword);  // 암호화된 비밀번호로 설정
        usersRepository.save(user);  // 사용자 저장
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

    // 사용자 정보 수정
    @Transactional
    public void updateUser(Long userId, Users updatedUser) {
        Users user = getUserById(userId);
        user.updateFields(updatedUser);  // 엔티티 내에서 필드 업데이트
        usersRepository.save(user);  // 변경된 내용 저장
    }

    // 사용자 삭제
    @Transactional
    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

	  // 초기화 (테스트용 하드코딩된 패스워드 수정)
	  /*
	  @PostConstruct public void init() { // 예시로 패스워드 하드코딩 (실제 배포에서는 적절한 암호화 및 보안조치가 필요) 
		  String sql = "UPDATE Users SET password = ? WHERE username = ?";
	  jdbcTemplate.update(sql,"$2a$10$XYZW.CUCSuyPv.cRAV5SeO0dLX.o.fiA0Nm/cZKt.vet5cetzC95K", "junseo"); }
	 */
}
