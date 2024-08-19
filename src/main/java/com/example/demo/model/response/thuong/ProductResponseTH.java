package com.example.demo.model.response.thuong;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseTH {

    private Long id;
    private String code;
    private String name;
    private BigDecimal price;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer status;
    private String imagePath;

    public ProductResponseTH(Long id, String code, String name, BigDecimal price, Date createdAt, Date updatedAt, String createdBy, String updatedBy, Integer status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.status = status;
    }
}
