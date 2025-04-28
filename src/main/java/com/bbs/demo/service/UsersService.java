package com.bbs.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;

	//test
	 @Autowired 
	 private JdbcTemplate jdbcTemplate; // JdbcTemplate을 서비스에서 사용
	
    

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
    	log.info("####로그인 시도: username = {}", username);
        return usersRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Username: " + username));
    }

    // 사용자 정보 수정
    @Transactional
    public void updateUser(Long userId, Users updatedUser) {
        Users user = getUserById(userId);  // 사용자 정보 가져오기
        user.updateFields(updatedUser);  // 엔티티 내에서 null 체크 후 필드 업데이트
        usersRepository.save(user);  // 변경된 내용 저장
    }

    // 사용자 삭제
    @Transactional
    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

	
	  // 초기화 (테스트용 하드코딩된 패스워드 수정)
	  
	  @PostConstruct public void init() { // 예시로 패스워드 하드코딩 (실제 배포에서는 적절한 암호화 및 보안조치가 필요) 
		  String sql = "UPDATE Users SET password = ? WHERE username = ?";
	  jdbcTemplate.update(sql,"$2a$10$XYZW.CUCSuyPv.cRAV5SeO0dLX.o.fiA0Nm/cZKt.vet5cetzC95K", "junseo"); }
	 
}
