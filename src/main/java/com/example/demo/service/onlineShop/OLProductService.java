package com.example.demo.service.onlineShop;

import com.example.demo.entity.Product;
import com.example.demo.model.response.onlineShop.ProductSaleDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

    public interface OLProductService {


        Page<ProductSaleDetails> filterProducts(
                String name,
                Set<String> colors,
                Set<String> sizes,
                Set<String> materials,
                Set<String> collars,
                Set<String> wrists,
                Double minPrice,
                Double maxPrice,
                Pageable pageable) ;
    }

