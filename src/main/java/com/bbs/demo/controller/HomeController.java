package com.bbs.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        return "home";
    }

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("member", new Member());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // 회원가입 페이지
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerPost(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        try {
            // 아이디 중복
            if (memberService.isUsernameDuplicated(member.getUsername())) {
                redirectAttributes.addFlashAttribute("registerError", "이미 사용 중인 아이디입니다.");
                return "redirect:/register";
            }
            // 비밀번호 암호화
            member.setPassword(passwordEncoder.encode(member.getPassword()));

            boolean result = memberService.register(member);
            
            if (result) {
                redirectAttributes.addFlashAttribute("registerSuccess", "회원가입이 완료되었습니다. 로그인해주세요.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("registerError", "회원가입 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
                return "redirect:/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("registerError", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/register";
        }
    }

    // 접근 거부
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

     @GetMapping("/ajax-error")
     public String ajaxError(@RequestParam(required = false) String message, Model model) {
         if (message != null && !message.isEmpty()) {
             model.addAttribute("errorMessage", message);
         }
         return "ajax-error";
     }
}