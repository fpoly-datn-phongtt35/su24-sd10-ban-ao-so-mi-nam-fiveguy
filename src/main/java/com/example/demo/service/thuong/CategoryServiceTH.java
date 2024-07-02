package com.example.demo.service.thuong;

import com.example.demo.entity.Category;
import com.example.demo.model.request.thuong.CategoryRequestTH;
import org.springframework.data.domain.Page;

public interface CategoryServiceTH {
    Page<Category> getCategories(int page, int size, String name, String sortField, String sortDirection);
    Category findById(Long id);
    Category create(CategoryRequestTH request);
    Category update(CategoryRequestTH request, Long id);
    Category updateStatus(Long id);
    Category delete(Long id);
}
