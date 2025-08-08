package com.danielschmitz.estoque.controller;

import com.danielschmitz.estoque.model.User;
import com.danielschmitz.estoque.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private  UserService userService;

    @GetMapping()
    public String index(
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping()
    public String handleSubmit(
            @RequestParam String name,
            @RequestParam String email,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal User user
    ){
        try {
            userService.save(user, name, email);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/profile";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Profile atualizado com sucesso");
        return "redirect:/";
    }
}
