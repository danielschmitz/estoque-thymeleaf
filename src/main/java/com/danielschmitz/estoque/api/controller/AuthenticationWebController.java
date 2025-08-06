package com.danielschmitz.estoque.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationWebController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}