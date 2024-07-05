package com.example.demo.service.onlineShop;

import com.example.demo.entity.Product;
import com.example.demo.model.response.onlineShop.ProductDetailsDTO;
import com.example.demo.model.response.onlineShop.ProductSaleDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

    public interface OLProductService2 {


        Page<ProductSaleDetails> filterProducts2(
                Set<Long> categoryIds, Set<Long> collarIds, Set<Long> wristIds,
                Set<Long> colorIds, Set<Long> sizeIds, Set<Long> materialIds,
                String searchTerm, Pageable pageable);



        ProductDetailsDTO getProductDetails(Long idProduct);

        Integer findPromotionalPriceByProductId(Long productId);

        BigDecimal getProductPriceById(Long productId);
    }

