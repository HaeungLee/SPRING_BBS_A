package com.bbs.demo.config;

import com.bbs.demo.mapper.MemberMapper;
import com.bbs.demo.model.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
 
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        Member member = memberMapper.findByUsername(username);  // 또는 login(username)

        HttpSession session = request.getSession();
        session.setAttribute("userId", member.getUserId());
        session.setAttribute("username", member.getUsername());
        session.setAttribute("nickname", member.getNickname());

        if ("Y".equals(member.getIsManager())) {
            session.setAttribute("isAdmin", true);
            response.sendRedirect("/admin/index");
        } else {
            response.sendRedirect("/");
        }
    }
}
