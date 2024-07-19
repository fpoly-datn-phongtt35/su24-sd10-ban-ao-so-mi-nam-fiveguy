package com.example.demo.model.response.sale;

import lombok.Data;

import java.math.BigDecimal;

@Data

public class ProductSaleDTO {

    private Long id;

    private String code;

    private String name;

    private String path;

    private BigDecimal price;

    private Integer promotionalPrice;

    public ProductSaleDTO(Long id, String code, String name, String path, BigDecimal price, Integer pricePromotion) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.path = path;
        this.price = price;
        this.promotionalPrice = pricePromotion;
    }
}
