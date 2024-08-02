package com.example.demo.model.response.thuong;

import com.example.demo.entity.*;
import lombok.*;

import java.util.Date;

@Data
public class ProductDetailResponseTH {

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
}
