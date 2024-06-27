package com.example.demo.restController.onlineShop;

import com.example.demo.model.response.onlineShop.ProductSaleDetails;
import com.example.demo.service.onlineShop.OLProductService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/home")
public class OLProductController2 {

    @Autowired
    private OLProductService2 productService;

    @GetMapping("/product/filter")
    public Page<ProductSaleDetails> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String colorIdParam,
            @RequestParam(required = false) String sizeIdParam,
            @RequestParam(required = false) String materialIdParam,
            @RequestParam(required = false) String collarIdParam,
            @RequestParam(required = false) String wristIdParam,
            @RequestParam(required = false) String categoryIdParam,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "createdAt") String sort) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Long colorId = parseId(colorIdParam);
        Long sizeId = parseId(sizeIdParam);
        Long materialId = parseId(materialIdParam);
        Long collarId = parseId(collarIdParam);
        Long wristId = parseId(wristIdParam);
        Long categoryId = parseId(categoryIdParam);

        return productService.filterProducts(
                categoryId, name, colorId, sizeId, materialId, collarId, wristId, pageable);
    }

    private Long parseId(String idString) {
        if (idString != null && !idString.isEmpty()) {
            try {
                return Long.parseLong(idString);
            } catch (NumberFormatException e) {
                // Handle parsing error if needed
                return null;
            }
        }
        return null;
    }



    @GetMapping("/products/filter2")
    public Page<ProductSaleDetails> filterProducts(
            @RequestParam(required = false) Set<String> categoryIds,
            @RequestParam(required = false) Set<String> collarIds,
            @RequestParam(required = false) Set<String> wristIds,
            @RequestParam(required = false) Set<String> colorIds,
            @RequestParam(required = false) Set<String> sizeIds,
            @RequestParam(required = false) Set<String> materialIds,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "createdAt") String sort) {

        // Chuyển đổi Set<String> sang Set<Long>
        Set<Long> categoryIdSet = convertToLongSet(categoryIds);
        Set<Long> collarIdSet = convertToLongSet(collarIds);
        Set<Long> wristIdSet = convertToLongSet(wristIds);
        Set<Long> colorIdSet = convertToLongSet(colorIds);
        Set<Long> sizeIdSet = convertToLongSet(sizeIds);
        Set<Long> materialIdSet = convertToLongSet(materialIds);

        // Xác định hướng sắp xếp và thuộc tính sắp xếp
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortBy;
        if ("price".equals(sort)) {
            sortBy = "promotionalPrice";
        } else {
            sortBy = "createdAt";
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return productService.filterProducts2(
                categoryIdSet, collarIdSet, wristIdSet, colorIdSet, sizeIdSet, materialIdSet, searchTerm, pageable);
    }

    private Set<Long> convertToLongSet(Set<String> stringSet) {
        Set<Long> longSet = new HashSet<>();
        if (stringSet != null) {
            for (String s : stringSet) {
                try {
                    longSet.add(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    // Xử lý lỗi chuyển đổi nếu cần thiết
                }
            }
        }
        return longSet;
    }



}