package com.example.demo.model.response.sale;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;

    private String code;

    private String name;

    private String path;

    private BigDecimal price;


    public ProductDTO(Long id, String code, String name, String path, BigDecimal price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.path = path;
        this.price = price;
    }
}
