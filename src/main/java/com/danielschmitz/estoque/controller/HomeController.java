package com.danielschmitz.estoque.controller;

import com.danielschmitz.estoque.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categoryCounts", productService.countProductsByCategory());
        return "index";
    }

}
