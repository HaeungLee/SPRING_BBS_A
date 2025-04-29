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
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // DB 컬럼명 명시적 지정
    private Integer user_Id; // Integer로 변경

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

    @Builder.Default 
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

    // 관리자 여부 체크 메소드
    public boolean isAdmin() {
        return "Y".equalsIgnoreCase(this.is_Manager);
    }

    // 비밀번호를 설정하는 메소드 (Lombok @Setter 대신)
    public void setPassword(String password) {
        this.password = password;
    }
    

    // updateFields 메소드 수정
    public void updateFields(Users updatedUser) {
        if (updatedUser.getUsername() != null) this.username = updatedUser.getUsername();
        if (updatedUser.getEmail() != null) this.email = updatedUser.getEmail();
        if (updatedUser.getNickname() != null) this.nickname = updatedUser.getNickname();
        if (updatedUser.getProfile_Img() != null) this.profile_Img = updatedUser.getProfile_Img();
        if (updatedUser.getPhone() != null) this.phone = updatedUser.getPhone();
        if (updatedUser.getAgreeMarketing() != null) this.agreeMarketing = updatedUser.getAgreeMarketing();
        if (updatedUser.getIs_Manager() != null) this.is_Manager = updatedUser.getIs_Manager();
        // 필요 시 다른 필드들도 추가 가능
    }
}
