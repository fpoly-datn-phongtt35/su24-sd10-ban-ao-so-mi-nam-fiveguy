package com.example.demo.service.nguyen;

import com.example.demo.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface NProductDetailService {

    public Page<ProductDetail> searchProductDetails(
            String productName,
            Long categoryId,
            Long materialId,
            Long wristId,
            Long collarId,
            Long sizeId,
            Long colorId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}
