package com.danielschmitz.estoque.service;

import com.danielschmitz.estoque.model.Category;
import com.danielschmitz.estoque.model.Product;
import com.danielschmitz.estoque.repository.CategoryRepository;
import com.danielschmitz.estoque.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> search(String name, Long categoryId) {
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasCategory = categoryId != null;
        if (hasName && hasCategory) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory_Id(name.trim(), categoryId);
        } else if (hasName) {
            return productRepository.findByNameContainingIgnoreCase(name.trim());
        } else if (hasCategory) {
            return productRepository.findByCategory_Id(categoryId);
        }
        return findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Product create(String name, String codigoDeBarras, Long categoryId) {
        validate(name, codigoDeBarras, categoryId);
        if (productRepository.existsByCodigoDeBarras(codigoDeBarras)) {
            throw new RuntimeException("Já existe um produto com este código de barras");
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria inválida"));
        Product p = new Product();
        p.setName(name.trim());
        p.setCodigoDeBarras(codigoDeBarras.trim());
        p.setCategory(category);
        return productRepository.save(p);
    }

    public Product update(Long id, String name, String codigoDeBarras, Long categoryId) {
        validate(name, codigoDeBarras, categoryId);
        Product existing = getById(id);
        if (!existing.getCodigoDeBarras().equals(codigoDeBarras)
                && productRepository.existsByCodigoDeBarras(codigoDeBarras)) {
            throw new RuntimeException("Já existe um produto com este código de barras");
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria inválida"));
        existing.setName(name.trim());
        existing.setCodigoDeBarras(codigoDeBarras.trim());
        existing.setCategory(category);
        return productRepository.save(existing);
    }

    public void delete(Long id) {
        Product existing = getById(id);
        productRepository.delete(existing);
    }

    private void validate(String name, String codigoDeBarras, Long categoryId) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("O nome do produto é obrigatório");
        }
        if (codigoDeBarras == null || codigoDeBarras.trim().isEmpty()) {
            throw new RuntimeException("O código de barras é obrigatório");
        }
        if (categoryId == null) {
            throw new RuntimeException("A categoria é obrigatória");
        }
    }
}
