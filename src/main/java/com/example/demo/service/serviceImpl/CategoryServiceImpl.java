package com.example.demo.service.serviceImpl;

import com.example.demo.entity.Category;
import com.example.demo.model.request.CategoryRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Override
    public Page<Category> getCategories(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        if (name == null || name.isEmpty())
        return repository.findAll(pageable);
        else return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Category create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setCreatedAt(new Date());
        category.setStatus(1);
        return repository.save(category);
    }

    @Override
    public Category update(CategoryRequest request, Long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(request.getName());
            category.setUpdatedAt(new Date());
            return repository.save(category);
        }
        return null;
    }

    @Override
    public Category delete(Long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            repository.delete(category);
            return category;
        }
        return null;
    }

    @Override
    public Category findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Category findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
