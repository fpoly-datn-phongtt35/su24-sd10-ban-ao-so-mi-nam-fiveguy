package com.example.demo.service.onlineShop;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OLCategoryService2 {

    List<Category> getAllActiveCategories();
    Category findCategoryByProductId(Long productId);
}
