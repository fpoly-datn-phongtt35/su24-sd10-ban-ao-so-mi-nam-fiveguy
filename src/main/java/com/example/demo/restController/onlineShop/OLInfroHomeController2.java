package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Category;
import com.example.demo.service.onlineShop.OLCategoryService2;
import com.example.demo.service.onlineShop.OLSaleService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLInfroHomeController2 {

    @Autowired
    private OLCategoryService2 olCategoryService2;

    @Autowired
    private OLSaleService2 saleService;

    //    Category
    @GetMapping("/categories")
    public List<Category> getAllActiveCategories() {
        return olCategoryService2.getAllActiveCategories();
    }


    @GetMapping("/salePaths")
    public List<String> getAllPaths() {
        return saleService.findAllPathsByStatus();
    }
}
