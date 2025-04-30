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
        return "home";
    }

    @Autowired
    private MemberService memberService;
    
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "returnUrl", required = false) String returnUrl, Model model) {
        model.addAttribute("member", new Member());
        if (returnUrl != null && !returnUrl.isEmpty()) {
            model.addAttribute("returnUrl", returnUrl);
        }
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
            
            // 디버깅: 회원가입 전 Member 객체 상태 확인
            System.out.println("회원가입 데이터: " + member.toString());
            
            boolean result = memberService.register(member);
            
            if (result) {
                redirectAttributes.addFlashAttribute("registerSuccess", "회원가입이 완료되었습니다. 로그인해주세요.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("registerError", "회원가입 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
                return "redirect:/register";
            }
        } catch (Exception e) {
            // 상세한 에러 메시지 출력
            e.printStackTrace();
            String errorMessage = e.getMessage();
            Throwable cause = e.getCause();
            while (cause != null) {
                errorMessage += " | Caused by: " + cause.getMessage();
                cause = cause.getCause();
            }
            
            redirectAttributes.addFlashAttribute("registerError", "회원가입 처리 중 오류가 발생했습니다: " + errorMessage);
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