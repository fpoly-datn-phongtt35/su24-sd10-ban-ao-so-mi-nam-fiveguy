package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Color;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.model.response.nguyen.ProductFilterResponse;
import com.example.demo.repository.nguyen.product.NImageRepository;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.repository.nguyen.product.NProductRepository;
import com.example.demo.service.nguyen.NProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NProductServiceImpl implements NProductService {

    @Autowired
    NProductRepository productRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;

    @Autowired
    NImageRepository imageRepository;

    @Override
    public BigDecimal getMaxPrice() {
        return productRepository.findMaxPrice();
    }

    @Override
    public BigDecimal getMinPrice() {
        return productRepository.findMinPrice();
    }

//    @Override
//    public String getImagePathByProductId(Long id) {
//        List<Image> images = imageRepository.findImagesByProductIdOrderByCreatedAtAsc(id);
//        return images.isEmpty() ? null : images.get(0).getPath();
//    }

    public List<Image> getImagesByProductAndColor(Long productId, Long colorId) {
        Product product = new Product();
        product.setId(productId);

        Color color = new Color();
        color.setId(colorId);

        return imageRepository.findByProductAndColor(product, color);
    }

    @Override
    public String getImagePathByProductId(Long id, Long colorId) {
        List<Image> images = imageRepository.findAllByProductIdAndColorIdAndStatus(id, colorId, 1);
        return images.isEmpty() ? null : images.get(0).getPath();
    }
}
