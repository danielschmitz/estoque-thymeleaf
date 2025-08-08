package com.danielschmitz.estoque.controller;

import com.danielschmitz.estoque.model.Category;
import com.danielschmitz.estoque.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "categories/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("title", "Nova Categoria");
        return "categories/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         RedirectAttributes redirectAttributes) {
        try {
            Category c = new Category();
            c.setName(name);
            categoryService.create(c);
            redirectAttributes.addFlashAttribute("successMessage", "Categoria criada com sucesso");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getById(id);
            model.addAttribute("category", category);
            model.addAttribute("title", "Editar Categoria");
            return "categories/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         RedirectAttributes redirectAttributes) {
        try {
            categoryService.update(id, name);
            redirectAttributes.addFlashAttribute("successMessage", "Categoria atualizada com sucesso");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Categoria removida com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/categories";
    }
}
