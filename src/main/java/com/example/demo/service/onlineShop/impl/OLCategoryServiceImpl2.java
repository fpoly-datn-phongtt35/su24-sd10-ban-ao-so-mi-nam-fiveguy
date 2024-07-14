package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Category;
import com.example.demo.repository.onlineShop.OLCategoryRepository2;
import com.example.demo.repository.onlineShop.OLSizeRepository2;
import com.example.demo.service.onlineShop.OLCategoryService2;
import com.example.demo.service.onlineShop.OLSizeService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OLCategoryServiceImpl2 implements OLCategoryService2 {

    @Autowired
    private OLCategoryRepository2 olCategoryRepository2;


    @Override
    public List<Category> getAllActiveCategories() {
        return olCategoryRepository2.findAllActiveCategories();
    }
}