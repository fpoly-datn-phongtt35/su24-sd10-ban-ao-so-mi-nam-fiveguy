package com.example.demo.model.response.nguyen;

import com.example.demo.entity.Color;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.entity.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDetailResponse {

    private Long id;
    private Integer quantity;
    private String barcode;

    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    private Integer status;

    private Product product;

    private Size size;
    private Color color;

    private String imagePath;


    private BigDecimal promotionalPrice;
}