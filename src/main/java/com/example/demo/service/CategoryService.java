package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.model.request.CategoryRequest;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Page<Category> getCategories(int page, int size, String name);
    Category findByName(String name);
    Category findById(Long id);
    Category create(CategoryRequest request);
    Category update(CategoryRequest request, Long id);
    Category delete(Long id);
}
