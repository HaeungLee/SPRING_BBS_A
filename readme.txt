주제선정 및 기획안, User table 작성 - 원석님

게시판 - post 한영님
- 구현한 점 : home.html 및 Post  (view , list)등 css
             글 작성 부분 CKEditor 적용
             카카오 맵 API 적용
             조회수(viewcount 증가 기능) - 글 작성자 본인이면 증가하지 않음
             작성자면 게시글 수정 삭제 가능


댓글 - comment 서혁님
  Spring Boot와 MyBatis, JSP, AJAX를 활용하여 구현한 댓글(Comment) 기능
  사용자 경험을 고려한 동적 UI와 RESTful API 구조를 통해, 게시글에 대한 댓글 작성, 조회, 수정, 삭제는 물론, 좋아요 및 대댓글 기능까지 포괄하는 직관적인 댓글 시스템 구현

- 구현한 점 : 댓글 CRUD 기능 : 게시글에 댓글을 작성하고, 조회하며, 자신이 작성한 댓글을 수정하거나 삭제 가능
             REST + AJAX 이용한 댓글 기능 : RESTful API를 사용하고 AJAX를 통해 페이지 새로고침 없이 댓글 기능이 동작하도록 구현
             좋아요 기능 : 댓글에 대해 좋아요를 누르거나 취소할 수 있으며, 해당 기능은 비동기로 즉시 반영
             대댓글 기능 :  댓글에는 답글을 작성할 수 있으며, parent_id를 기준으로 계층 구조가 유지
             게시글 목록에서 댓글이 몇 개 있는지 표시 : 게시글 목록 화면에서 각 게시글에 달린 댓글 수가 표시되어 댓글 수 쉽게 확인
             권한 기반 수정/삭제: 로그인한 사용자만 댓글을 작성할 수 있고, 작성자 본인만 해당 댓글을 수정하거나 삭제
             공유 DB 설정 : Railway와 같은 외부 공유 데이터베이스를 사용하여 팀 단위 협업이 가능하도록 설정
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>댓글 시스템 README</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      line-height: 1.6;
      margin: 40px;
    }
    h1, h2 {
      color: #2c3e50;
    }
    ul {
      list-style-type: disc;
      margin-left: 20px;
    }
    pre {
      background: #f4f4f4;
      padding: 10px;
      border-left: 4px solid #3498db;
      overflow-x: auto;
    }
  </style>
</head>
<body>
  <h1>댓글 - Comment</h1>
  <p>
    <strong>Spring Boot</strong>와 <strong>MyBatis</strong>, <strong>JSP</strong>, <strong>AJAX</strong>를 활용하여 구현한 댓글(Comment) 기능입니다.
  </p>
  <p>
    사용자 경험을 고려한 동적 UI와 RESTful API 구조를 통해, 게시글에 대한 댓글 작성, 조회, 수정, 삭제는 물론, 좋아요 및 대댓글 기능까지 포괄하는 직관적인 댓글 시스템을 구현하였습니다.
  </p>

  <h2>구현한 기능</h2>
  <ul>
    <li><strong>댓글 CRUD 기능</strong>: 게시글에 댓글을 작성, 조회, 수정, 삭제 가능</li>
    <li><strong>REST + AJAX</strong>: 비동기 방식으로 댓글 기능 구현 (페이지 새로고침 없음)</li>
    <li><strong>좋아요 기능</strong>: 댓글에 좋아요/취소 가능, AJAX로 즉시 반영</li>
    <li><strong>대댓글 기능</strong>: parent_id 기반 계층 구조 유지, 트리 형태 표시</li>
    <li><strong>댓글 수 표시</strong>: 게시글 목록에서 각 게시글에 달린 댓글 수 확인 가능</li>
    <li><strong>권한 기반 수정/삭제</strong>: 로그인 사용자만 댓글 작성 가능, 작성자만 수정/삭제 가능</li>
    <li><strong>공유 DB 설정</strong>: Railway 등 외부 공유 DB로 팀 협업 지원</li>
  </ul>

  <h2>댓글 관련 기능 설명</h2>
  <ul>
    <li><strong>댓글 작성/조회/수정/삭제</strong>: 로그인한 사용자만 가능</li>
    <li><strong>작성자 권한</strong>: 본인만 댓글 수정/삭제 가능</li>
    <li><strong>소프트 삭제</strong>: 삭제 시 자식 댓글(대댓글)도 함께 논리적 삭제</li>
  </ul>

  <h2>좋아요 기능</h2>
  <ul>
    <li>AJAX 기반 좋아요 토글</li>
    <li>중복 좋아요 방지</li>
    <li>비로그인 사용자는 좋아요 제한</li>
  </ul>

  <h2>대댓글 (답글)</h2>
  <ul>
    <li>parent_id 필드 기준 계층형 구조 유지</li>
    <li>트리 형태로 댓글 표시</li>
  </ul>

  <h2>인증 및 권한</h2>
  <ul>
    <li>세션 기반 로그인 확인</li>
    <li>댓글 작성/수정/삭제 권한 확인</li>
    <li>관리자 권한자는 모든 댓글 삭제 가능</li>
  </ul>
</body>
</html>



파일 - file 소연님
- 구현한 점 : 파일 업로드 기능
             파일 다운로드 기능
             파일 업로드한 첫번째 이미지가 썸네일 기능 구현

관리자 - admin 준서님
- 구현한 점 : Spring Security 적용
             관리자 페이지 css
             관리자 로그인시 관리자 대시보드로 이동
             관리자시 파일, 게시판, 댓글, 회원관리 수정 삭제 기능


페이지 - pagination / search - 지선님
- 구현한 점 : 검색창 및 검색 기능 구현
             Suggest 기능(자동완성) 구현
             Ajax를 활용한 pagination 기능 구현
