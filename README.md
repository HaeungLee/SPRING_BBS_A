# 📋 Spring Boot 게시판 시스템

![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-DC143C?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEyIDJMMjIgN1YxN0wxMiAyMkwyIDE3VjdMMTIgMloiIGZpbGw9IndoaXRlIi8+Cjwvc3ZnPgo=&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)

> Spring Boot와 MyBatis를 활용한 풀스택 게시판 시스템  
> **TDD 방식**과 **Clean Architecture** 원칙을 준수하여 개발된 팀 프로젝트

---

## 🎥 시연 영상

> 📹 **데모 영상 링크**: ![Uploading board.gif…]()


### 주요 시연 내용
- 회원가입 및 로그인 프로세스
- 게시글 작성 및 파일 업로드
- 실시간 댓글 시스템 및 대댓글 기능
- 관리자 권한으로 시스템 관리
- 검색 및 페이지네이션 기능

---

## 🚀 기술 스택

### Backend
- **Spring Boot**: 메인 프레임워크
- **Spring Security**: 인증 및 권한 관리
- **MyBatis**: ORM 및 데이터베이스 연동
- **Java**: 프로그래밍 언어

### Frontend
- **JSP**: 서버사이드 렌더링
- **JavaScript**: 클라이언트 사이드 로직
- **AJAX**: 비동기 통신
- **Bootstrap**: 반응형 UI 프레임워크
- **CKEditor**: WYSIWYG 에디터

### Database
- **MySQL**: 관계형 데이터베이스
- **Railway**: 클라우드 데이터베이스 호스팅

### External API
- **카카오 맵 API**: 지도 서비스 연동

---

## 📊 시스템 아키텍처

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Presentation  │    │     Service     │    │   Data Access   │
│     Layer       │◄──►│     Layer       │◄──►│     Layer       │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ • Controller    │    │ • Service Impl  │    │ • Mapper        │
│ • JSP Templates │    │ • Business      │    │ • MyBatis       │
│ • AJAX/JS       │    │   Logic         │    │ • MySQL         │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🎯 핵심 기능

## 👥 회원 시스템

### 🔐 인증 및 보안
- **Spring Security** 기반 인증 시스템
- **BCrypt** 암호화를 통한 안전한 비밀번호 저장
- 평문 → 암호화 **마이그레이션** 서비스
- 세션 기반 로그인 상태 관리

### 👤 회원 관리
| 기능 | 설명 |
|------|------|
| 회원 가입 | 닉네임, 아이디, 비밀번호 등록 |
| 로그인/로그아웃 | Spring Security 연동 |
| 권한 관리 | 일반 사용자 / 관리자 구분 |
| 비밀번호 암호화 | BCrypt 적용 |

### 📁 관련 파일
- `MemberService`, `MemberMapper`, `Member` - 계층별 구조
- `SecurityConfig.java` - Spring Security 설정
- `CustomAuthSuccessHandler.java` - 로그인 후 처리
- `PasswordMigrationService.java` - 비밀번호 마이그레이션

---

## 📝 게시판 시스템

### ✨ 주요 기능
- **CKEditor** 적용 WYSIWYG 에디터
- **카카오 맵 API** 연동으로 지도 삽입 가능
- 조회수 자동 증가 (작성자 본인 제외)
- 작성자 권한 기반 수정/삭제 제어

### 📄 페이지 구성
- `home.html` - 메인 홈 페이지
- `post/list` - 게시글 목록
- `post/view` - 게시글 상세보기
- `post/form` - 게시글 작성/수정

### 🛡️ 권한 제어
- 작성자만 수정/삭제 가능
- 관리자는 모든 게시글 관리 가능

---

## 📁 파일 관리 시스템

### 📤 파일 업로드
- 게시글 작성 시 **다중 파일 업로드** 지원
- 이미지 파일 자동 **썸네일** 생성
- 첫 번째 이미지를 리스트 썸네일로 자동 지정

### 📥 파일 다운로드
- 안전한 파일 다운로드 시스템
- 파일 접근 권한 검증

### 🖼️ 이미지 처리
- 자동 썸네일 생성
- 이미지 최적화

---

## 🔍 검색 및 페이지네이션

### 🎯 고급 검색
- **다중 조건 검색**: 제목, 내용, 작성자
- **자동완성 기능**: AJAX 기반 실시간 검색어 추천
- **검색 결과 하이라이팅**

### 📄 페이지네이션
- **AJAX 기반** 비동기 페이지 전환
- 빠른 사용자 경험(UX) 제공
- SEO 친화적 URL 구조

### ⚡ 성능 최적화
- 검색 인덱스 활용
- 페이지 캐싱 적용

---

## 💬 댓글 시스템

### 🎨 댓글 기능
- **RESTful API** 구조의 댓글 CRUD
- **AJAX** 기반 실시간 댓글 처리
- **대댓글**(답글) 기능으로 계층형 구조
- 댓글 **좋아요** 기능

### 🔒 권한 관리
- 로그인 사용자만 댓글 작성 가능
- 작성자 본인만 수정/삭제 가능
- **소프트 삭제**로 데이터 무결성 보장

### 📊 부가 기능
- 게시글별 댓글 수 표시
- 좋아요 중복 방지
- 실시간 댓글 수 업데이트

### 🏗️ 기술적 특징
- `parent_id` 기반 계층형 댓글 구조
- AJAX 비동기 처리로 페이지 새로고침 없는 UX
- RESTful API 설계

---

## 🛠️ 관리자 시스템

### 👨‍💼 관리자 권한
| 관리 영역 | 기능 |
|-----------|------|
| 👥 **회원 관리** | 회원 목록 조회, 계정 삭제 |
| 📝 **게시물 관리** | 전체 게시글 조회 및 삭제 |
| 💬 **댓글 관리** | 모든 댓글 조회 및 삭제 |
| 📁 **파일 관리** | 업로드 파일 정보 및 삭제 |

### 🔧 관리 기능
- 통합 관리자 대시보드
- 일괄 처리 기능
- 통계 및 모니터링
- 시스템 설정 관리

### 🛡️ 보안 강화
- 관리자 권한 검증
- 민감한 작업에 대한 확인 절차
- 관리자 활동 로그 기록

---

## 🚀 프로젝트 실행 방법

### 📋 사전 요구사항
- **Java 11** 이상
- **MySQL 8.0** 이상
- **Gradle** 7.0 이상

### ⚙️ 환경 설정
1. **데이터베이스 설정**
   ```sql
   CREATE DATABASE spring_bbs;
   ```

2. **application.properties 설정**
   ```properties
   # 데이터베이스 연결 정보
   spring.datasource.url=jdbc:mysql://localhost:3306/spring_bbs
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **프로젝트 실행**
   ```bash
   # Windows
   gradlew bootRun
   
   # Linux/Mac
   ./gradlew bootRun
   ```

4. **브라우저에서 확인**
   ```
   http://localhost:8080
   ```

---

## 📱 반응형 디자인

- **Bootstrap 5** 기반 모바일 친화적 UI
- **데스크톱**, **태블릿**, **모바일** 완벽 지원
- 직관적이고 현대적인 사용자 인터페이스

---

## 🔧 개발 원칙

### 🎯 TDD (Test-Driven Development)
- **테스트 우선** 개발 방식 적용
- 단위 테스트 및 통합 테스트 구현
- 코드 품질 및 안정성 보장

### 🏗️ Clean Architecture
- **SOLID 원칙** 준수
- 계층별 책임 분리
- 의존성 역전을 통한 유연한 구조

### 🇰🇷 한국어 최적화
- 한국어 사용자를 위한 UX/UI 설계
- 한글 검색 및 정렬 최적화
- 한국 웹 표준 준수

### ⚡ 성능 최적화
- **최소한의 의존성**으로 경량화
- 데이터베이스 쿼리 최적화
- AJAX를 통한 부분 렌더링

---

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/com/bbs/demo/
│   │   ├── config/          # 설정 파일
│   │   ├── controller/      # 컨트롤러 계층
│   │   ├── service/         # 서비스 계층
│   │   ├── mapper/          # 데이터 접근 계층
│   │   └── model/           # 도메인 모델
│   └── resources/
│       ├── mapper/          # MyBatis XML
│       ├── templates/       # JSP 템플릿
│       └── static/          # 정적 리소스
└── test/                    # 테스트 코드
```

---

## 🤝 팀 구성원

| 역할 | 담당 시스템 | 주요 기능 |
|------|-------------|-----------|
| **팀장** | 전체 아키텍처, 회원 시스템 | 프로젝트 설계, Spring Security |
| **백엔드** | 게시판, 파일 시스템 | CRUD, 파일 업로드/다운로드 |
| **백엔드** | 댓글, 검색 시스템 | 댓글 CRUD, 검색 알고리즘 |
| **프론트엔드** | UI/UX, 관리자 시스템 | 화면 설계, 관리자 대시보드 |

---

## 📈 향후 개선 계획

### 🔮 추가 기능
- [ ] **실시간 알림** 시스템 (WebSocket)
- [ ] **이메일 인증** 회원가입
- [ ] **소셜 로그인** (Google, Naver, Kakao)
- [ ] **게시글 태그** 시스템
- [ ] **북마크** 기능

### 🚀 성능 개선
- [ ] **Redis** 캐시 도입
- [ ] **CDN** 연동
- [ ] **이미지 압축** 최적화
- [ ] **데이터베이스 인덱싱** 최적화

### 🛡️ 보안 강화
- [ ] **JWT** 토큰 기반 인증
- [ ] **CSRF** 보호 강화
- [ ] **XSS** 방어 구현
- [ ] **API 율제한** 적용

---

## 📞 문의사항

프로젝트에 대한 문의사항이나 개선 제안이 있으시면 언제든 연락주세요!

**📧 Email**: [팀 이메일 주소]  
**📱 GitHub**: [GitHub 저장소 링크]  

---

<div align="center">

**🏆 Spring Boot 게시판 시스템**  
*TDD와 Clean Architecture를 통한 고품질 웹 애플리케이션*

![Footer Image](https://img.shields.io/badge/Made_with-❤️-red.svg)
![Team](https://img.shields.io/badge/Team-Spring_Warriors-blue.svg)

</div>
