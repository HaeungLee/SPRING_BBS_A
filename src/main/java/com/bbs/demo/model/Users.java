package com.bbs.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드 포함 생성자
@Builder
public class Users{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
	@Column(nullable = false, length = 50, unique = true)
    private String username;
	@Column(nullable = false, length = 255)
    private String password;
	@Column(nullable = false, length = 100, unique = true)
    private String email;
	@Column(length = 50)
    private String nickname;
	@Column(length = 255)
	private String profile_img;
	@Builder.Default
	@Column(name = "is_manager", columnDefinition = "ENUM('Y','N') default 'N'")
	private String isManager = "N";
	@Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
	@Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	public boolean isAdmin() {
		return "Y".equalsIgnoreCase(this.isManager);
	}
}
