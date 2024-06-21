package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Product;
import com.example.demo.model.response.onlineShop.ProductSaleDetails;
import com.example.demo.service.onlineShop.OLProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/home")
public class OLProductController {

    @Autowired
    private OLProductService productService;

    @GetMapping("/product/filter")
    public Page<ProductSaleDetails> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Set<String> colorNames,
            @RequestParam(required = false) Set<String> sizeNames,
            @RequestParam(required = false) Set<String> materialNames,
            @RequestParam(required = false) Set<String> collarNames,
            @RequestParam(required = false) Set<String> wristNames,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "price") String sort) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        return productService.filterProducts(name, colorNames, sizeNames, materialNames, collarNames, wristNames, minPrice, maxPrice, pageable);
    }

}