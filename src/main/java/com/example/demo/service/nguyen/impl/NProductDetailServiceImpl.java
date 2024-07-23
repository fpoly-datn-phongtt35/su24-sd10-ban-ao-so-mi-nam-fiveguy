package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NProductDetailServiceImpl implements NProductDetailService {

    @Autowired
    private NProductDetailRepository productDetailRepository;

    @Override
    public Page<ProductDetail> searchProductDetails(String productName,
                                                    Long categoryId,
                                                    Long materialId,
                                                    Long wristId,
                                                    Long collarId,
                                                    Long sizeId,
                                                    Long colorId,
                                                    BigDecimal minPrice,
                                                    BigDecimal maxPrice,
                                                    Pageable pageable) {

        return productDetailRepository
                .searchProductDetails(productName, categoryId, materialId, wristId, collarId,
                        sizeId, colorId, minPrice, maxPrice, 1, pageable);
    }
}
