package com.bbs.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbs.demo.model.Member;
import com.bbs.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // home.html
    }

    @Autowired
    private MemberService memberService;
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("member", new Member());
        return "login"; 
    }
    
    @PostMapping("/login")
    public String processLogin(@ModelAttribute Member member, HttpSession session, RedirectAttributes redirectAttributes) {
        Member loggedInMember = memberService.login(member.getUsername(), member.getPassword());
        
        if (loggedInMember != null) {
            session.setAttribute("userId", loggedInMember.getUserId());
            session.setAttribute("username", loggedInMember.getUsername());
            session.setAttribute("nickname", loggedInMember.getNickname());
            
            //관리자 확인
            if ("Y".equals(loggedInMember.getIsManager())) {
                session.setAttribute("isAdmin", true);
            }            
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // 회원가입 페이지
    @GetMapping("/register")
    public String register() {
        return "register"; // register.html
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerPost(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String name,
                               @RequestParam String email) {
        // 실제 DB 저장 로직 생략 (UserService 등 필요)
        // 저장 성공 후 로그인 페이지로 리디렉션
        return "redirect:/login?registered";
    }

    // 접근 거부 페이지
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // access-denied.html
    }

     @GetMapping("/ajax-error")
     public String ajaxError(@RequestParam(required = false) String message, Model model) {
         if (message != null && !message.isEmpty()) {
             model.addAttribute("errorMessage", message);
         }
         return "ajax-error";  
     }
}