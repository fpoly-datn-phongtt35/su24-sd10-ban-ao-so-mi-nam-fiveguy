package com.example.demo.model.response.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.ProductDetail;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillDetailResponse {

    private Long id;

    private int quantity;

    private BigDecimal price;

    private BigDecimal promotionalPrice;

    private int status;

    private Bill bill;

    private ProductDetail productDetail;

    private int defectiveProduct;

    private String imagePath;
}
