package com.example.demo.restController.common;

import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.common.ProductDetailDTO;
import com.example.demo.service.common.ProductDetailServiceCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/product-details")
public class ProductDetailControllerCommon {


    @Autowired
    private ProductDetailServiceCommon productDetailService;

    @GetMapping
    public Page<ProductDetailDTO> getFilteredProductDetails(
            @RequestParam(required = false) List<Long> colorIds,
            @RequestParam(required = false) List<Long> sizeIds,
            @RequestParam(required = false) List<Long> materialIds,
            @RequestParam(required = false) List<Long> collarIds,
            @RequestParam(required = false) List<Long> wristIds,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productDetailService.getFilteredProductDetails(
                colorIds, sizeIds, materialIds, collarIds, wristIds, categoryIds, search, pageable);
    }



}
