package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Category;
import com.example.demo.model.request.thuong.CategoryRequestTH;
import com.example.demo.repository.thuong.CategoryRepositoryTH;
import com.example.demo.service.thuong.CategoryServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceTHImpl implements CategoryServiceTH {
    @Autowired
    private CategoryRepositoryTH repository;

    @Override
    public Page<Category> getCategories(int page, int size, String name, String sortField, String sortDirection, Integer status) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        if (name == null || name.isEmpty())
            return repository.findAllAndStatus(status, pageable);
        return repository.findByNameContainingIgnoreCaseAndStatus(name, status, pageable);
    }

    @Override
    public List<Category> findAllByStatus(Integer status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Category create(CategoryRequestTH request) {
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
    public Category update(CategoryRequestTH request, Long id) {
        Category existingCategory = repository.findByName(request.getName());
        if (existingCategory != null && id != existingCategory.getId()) {
            throw new DuplicateException("Trùng tên nhóm sản phẩm", "name");
        }
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
