package com.example.demo.restController.nguyen;

import com.example.demo.entity.ProductDetail;
import com.example.demo.service.nguyen.NProductDetailService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/productDetail")
public class ProductDetailRestController {

    @Autowired
    private NProductDetailService productDetailService;

    @GetMapping("/page")
    public ResponseEntity<?> searchProductDetails(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Long wristId,
            @RequestParam(required = false) Long collarId,
            @RequestParam(required = false) Long sizeId,
            @RequestParam(required = false) Long colorId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
//        System.out.println(categoryId);
        return ResponseEntity.ok(productDetailService
                .searchProductDetails(productName, categoryId, materialId, wristId, collarId,
                        sizeId, colorId, minPrice == null ? null : BigDecimal.valueOf(minPrice), maxPrice== null ? null : BigDecimal.valueOf(maxPrice), pageable));
    }

}