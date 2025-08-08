package com.danielschmitz.estoque.controller;

import com.danielschmitz.estoque.model.Product;
import com.danielschmitz.estoque.service.CategoryService;
import com.danielschmitz.estoque.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(@RequestParam(required = false) String name,
                        @RequestParam(required = false) Long categoryId,
                        Model model) {
        List<Product> products = productService.search(name, categoryId);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("filterName", name);
        model.addAttribute("filterCategoryId", categoryId);
        return "products/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("title", "Novo Produto");
        return "products/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam String codigoDeBarras,
                         @RequestParam Long categoryId,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        try {
            productService.create(name, codigoDeBarras, categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "Produto criado com sucesso");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getById(id);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("title", "Editar Produto");
            return "products/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String codigoDeBarras,
                         @RequestParam Long categoryId,
                         RedirectAttributes redirectAttributes) {
        try {
            productService.update(id, name, codigoDeBarras, categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "Produto atualizado com sucesso");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Produto removido com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }
}
