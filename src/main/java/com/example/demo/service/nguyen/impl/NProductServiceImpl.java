package com.example.demo.service.nguyen.impl;

import com.example.demo.model.response.nguyen.ProductFilterResponse;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.repository.nguyen.product.NProductRepository;
import com.example.demo.service.nguyen.NProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NProductServiceImpl implements NProductService {

    @Autowired
    NProductRepository productRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;


    @Override
    public BigDecimal getMaxPrice() {
        return productRepository.findMaxPrice();
    }

    @Override
    public BigDecimal getMinPrice() {
        return productRepository.findMinPrice();
    }
}
