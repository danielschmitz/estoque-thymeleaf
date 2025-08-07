package com.danielschmitz.estoque.controller;

import com.danielschmitz.estoque.model.User;
import com.danielschmitz.estoque.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {

        try {
            userService.register(new User(null, name, email, password));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Usuário cadastrado com sucesso! Faça o login");
        return "redirect:/login";
    }
}