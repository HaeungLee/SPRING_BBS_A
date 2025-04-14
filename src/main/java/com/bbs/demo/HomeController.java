package com.bbs.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";  // index.html 렌더링
    }
    @GetMapping("/test01")
    public String test01() {
        return "TEST01";
    }

    @GetMapping("/test02")
    public String test02() {
        return "TEST02";
    }

    @GetMapping("/test03")
    public String test03() {
        return "TEST03";
    }

    @GetMapping("/test04")
    public String test04() {
        return "TEST04";
    }

    @GetMapping("/test05")
    public String test05() {
        return "TEST05";
    }

    @GetMapping("/test06")
    public String test06() {
        return "TEST06";
    }
}