package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.onlineShop.OLProductDetailRepository2;
import com.example.demo.service.onlineShop.OLProductDetailService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OLProductDetailServiceImpl2 implements OLProductDetailService2 {

    @Autowired
    private OLProductDetailRepository2 olProductDetailRepository2;

    @Override
    public List<ProductDetail> findByProduct(Product product) {
        return olProductDetailRepository2.findByProductAndStatus(product,1);
    }

    @Override
    public ProductDetail getProductDetail(Long productId, Long sizeId, Long colorId) {
        return olProductDetailRepository2.findByProductIdAndSizeIdAndColorId(productId, sizeId, colorId);
    }

    @Override
    public Optional<ProductDetail> findById(Long productDetailId) {
        return olProductDetailRepository2.findById(productDetailId);
    }

    @Override
    public ProductDetail save(ProductDetail productDetail) {
        return olProductDetailRepository2.save(productDetail);
    }

}
