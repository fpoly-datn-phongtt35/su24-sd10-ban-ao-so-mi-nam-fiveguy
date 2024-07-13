package com.example.demo.model.response.common;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data

public class ProductDetailDTO {

    private Long id;
    private int quantity;
    private String barcode;
    private BigDecimal price;
    private Integer promotionalPrice;
    private int status;
    private String productName;
    private String categoryName;
    private String materialName;
    private String wristName;
    private String collarName;
    private String sizeName;
    private String colorName;

    // Getters and Setters


    public ProductDetailDTO(Long id, int quantity, String barcode,  BigDecimal price, Integer promotionalPrice, int status, String productName, String categoryName, String materialName, String wristName, String collarName, String sizeName, String colorName) {
        this.id = id;
        this.quantity = quantity;
        this.barcode = barcode;
        this.price = price;
        this.promotionalPrice = promotionalPrice;
        this.status = status;
        this.productName = productName;
        this.categoryName = categoryName;
        this.materialName = materialName;
        this.wristName = wristName;
        this.collarName = collarName;
        this.sizeName = sizeName;
        this.colorName = colorName;
    }
}