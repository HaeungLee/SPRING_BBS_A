package com.bbs.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bbs.demo.model.Member;
import com.bbs.demo.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    // 필드 주입 제거하고 ApplicationContext를 통해 필요할 때 가져오도록 변경
    @Autowired
    private ApplicationContext applicationContext;
    
    // memberService를 가져오는 private 메소드 추가
    private MemberService getMemberService() {
        return applicationContext.getBean(MemberService.class);
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 세션에 Spring Security의 Authentication 객체도 함께 저장
        HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        
        // 지연 로딩 방식으로 MemberService 가져오기
        Member member = getMemberService().getMemberByUsername(username);
        if (member != null) {
            session.setAttribute("userId", member.getUserId());
            session.setAttribute("username", member.getUsername());
            session.setAttribute("nickname", member.getNickname());
            
            if ("Y".equals(member.getIsManager())) {
                session.setAttribute("isAdmin", true);
            }
        }
        
        super.onAuthenticationSuccess(request, response, authentication);
    }
}