package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(HttpSession session) {

        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }

        return "login";
    }
}