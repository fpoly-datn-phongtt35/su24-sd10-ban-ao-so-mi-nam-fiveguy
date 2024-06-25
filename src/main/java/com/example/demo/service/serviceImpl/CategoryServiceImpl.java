package com.example.demo.service.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Category;
import com.example.demo.model.request.CategoryRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Override
    public Page<Category> getCategories(int page, int size, String name, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if (name == null || name.isEmpty())
        return repository.findAll(pageable);
        else return repository.findByNameContainingIgnoreCase(name,pageable);
    }

    @Override
    public Category create(CategoryRequest request) {
        Category existingCategory = repository.findByName(request.getName());
        if (existingCategory != null) {
            throw new DuplicateException("Trùng tên nhóm sản phẩm", "name");
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setCreatedAt(new Date());
        category.setStatus(1);
        return repository.save(category);
    }

    @Override
    public Category update(CategoryRequest request, Long id) {
        Category existingCategory = repository.findByName(request.getName());
        if (existingCategory != null && id != existingCategory.getId()) {
            throw new DuplicateException("Trùng tên nhóm sản phẩm", "name");
        }
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(request.getName());
            category.setUpdatedAt(new Date());
            category.setStatus(1);
            return repository.save(category);
        }
        return null;
    }

    @Override
    public Category updateStatus(Long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setUpdatedAt(new Date());
            category.setStatus(1);
            return repository.save(category);
        }
        return null;
    }

    @Override
    public Category delete(Long id) {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setStatus(0);
            return repository.save(category);
        }
        return null;
    }

    @Override
    public Category findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
