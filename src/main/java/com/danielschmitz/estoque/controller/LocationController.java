package com.danielschmitz.estoque.controller;

import com.danielschmitz.estoque.model.Location;
import com.danielschmitz.estoque.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public String index(Model model) {
        List<Location> locations = locationService.findAll();
        model.addAttribute("locations", locations);
        return "locations/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("location", new Location());
        model.addAttribute("title", "Novo Local");
        return "locations/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         RedirectAttributes redirectAttributes) {
        try {
            Location l = new Location();
            l.setName(name);
            locationService.create(l);
            redirectAttributes.addFlashAttribute("successMessage", "Local criado com sucesso");
            return "redirect:/locations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/locations/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Location location = locationService.getById(id);
            model.addAttribute("location", location);
            model.addAttribute("title", "Editar Local");
            return "locations/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/locations";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         RedirectAttributes redirectAttributes) {
        try {
            locationService.update(id, name);
            redirectAttributes.addFlashAttribute("successMessage", "Local atualizado com sucesso");
            return "redirect:/locations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/locations/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            locationService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Local removido com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/locations";
    }
}
