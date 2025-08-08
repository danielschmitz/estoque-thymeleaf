package com.danielschmitz.estoque.service;

import com.danielschmitz.estoque.model.Category;
import com.danielschmitz.estoque.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public Category create(Category category) {
        validateName(category.getName());
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new RuntimeException("Já existe uma categoria com este nome");
        }
        return categoryRepository.save(category);
    }

    public Category update(Long id, String name) {
        validateName(name);
        Category existing = getById(id);
        if (!existing.getName().equalsIgnoreCase(name)
                && categoryRepository.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Já existe uma categoria com este nome");
        }
        existing.setName(name);
        return categoryRepository.save(existing);
    }

    public void delete(Long id) {
        Category existing = getById(id);
        categoryRepository.delete(existing);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("O nome da categoria é obrigatório");
        }
    }
}
