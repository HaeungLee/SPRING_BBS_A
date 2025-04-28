package com.bbs.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users") // 실제 테이블명이 "Users"라고 가정
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users { // 클래스명 단수형으로 변경 (권장)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // DB 컬럼명 명시적 지정
    private Long user_Id; // Java 필드명 camelCase

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(length = 50)
    private String nickname;

    @Column(length = 255)
    private String profile_Img; // DB 컬럼명 profile_img와 매핑 필요 시 @Column(name = "profile_img")

    @Builder.Default
    @Column(name = "is_manager", columnDefinition = "CHAR(1) default 'N'") // ENUM → CHAR(1)
    private String is_Manager = "N";
    @Column(length = 20)
    private String phone;
    @Column(nullable = false)
    private Boolean agreeMarketing = false;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_At;

    @Column(name = "updated_at")
    private LocalDateTime updated_At;

    @PrePersist
    protected void onCreate() {
        created_At = LocalDateTime.now();
        updated_At = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_At = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return "Y".equalsIgnoreCase(this.is_Manager);
    }

    // Lombok @Setter 대신 필요 시 수동 추가
    public void setPassword(String password) {
        this.password = password;
    }

	public void updateFields(Users updatedUser) {
		this.username = username;
		this.email = email;
	}
}
