package com.example.demo.service.onlineShop;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OLProductDetailService2 {

     List<ProductDetail> findByProduct(Long id);

     ProductDetail getProductDetail( Long productId,Long sizeId,Long colorId);

     Optional<ProductDetail> findById(Long productDetailId);

     ProductDetail save(ProductDetail productDetail);
}
