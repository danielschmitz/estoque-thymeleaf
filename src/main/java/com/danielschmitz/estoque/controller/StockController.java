package com.danielschmitz.estoque.controller;

import com.danielschmitz.estoque.model.StockMovementType;
import com.danielschmitz.estoque.service.LocationService;
import com.danielschmitz.estoque.service.ProductService;
import com.danielschmitz.estoque.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public String index(@RequestParam(required = false) Long productId,
                        @RequestParam(required = false) Long locationId,
                        Model model) {
        model.addAttribute("stocks", stockService.search(productId, locationId));
        model.addAttribute("products", productService.findAll());
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("selectedProductId", productId);
        model.addAttribute("selectedLocationId", locationId);
        model.addAttribute("balance", stockService.getBalance(productId, locationId));
        return "stocks/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("movementTypes", StockMovementType.values());
        model.addAttribute("title", "Novo Movimento de Estoque");
        return "stocks/form";
    }

    @PostMapping
    public String create(@RequestParam Long productId,
                         @RequestParam Long locationId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
                         @RequestParam BigDecimal price,
                         @RequestParam BigDecimal quantity,
                         @RequestParam StockMovementType movementType,
                         RedirectAttributes redirectAttributes) {
        try {
            stockService.createMovement(productId, locationId, entryDate, expiryDate, price, quantity, movementType);
            redirectAttributes.addFlashAttribute("successMessage", "Movimento registrado com sucesso");
            return "redirect:/stocks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stocks/new";
        }
    }
}
