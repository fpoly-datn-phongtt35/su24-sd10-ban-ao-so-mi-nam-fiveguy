package com.example.demo.model.response.onlineShop;

import com.example.demo.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class ProductSaleDetails {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer discountPrice;
    private Integer promotionalPrice;
    private Integer saleValue;
    private Integer discountType;
    private String imagePath;
    private Product product;

    public ProductSaleDetails(Long productId, String productName, BigDecimal productPrice, Integer discountPrice, Integer promotionalPrice, Integer saleValue, Integer discountType, String imagePath, Product product) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.promotionalPrice = promotionalPrice;
        this.saleValue = saleValue;
        this.discountType = discountType;
        this.imagePath = imagePath;
        this.product = product;
    }


// Getters and setters
}
