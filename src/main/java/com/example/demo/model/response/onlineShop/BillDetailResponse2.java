package com.example.demo.model.response.onlineShop;

import com.example.demo.entity.Bill;
import com.example.demo.entity.ProductDetail;
import lombok.Data;

import java.math.BigDecimal;
@Data

public class BillDetailResponse2 {

    private Long id;

    private int quantity;

    private BigDecimal price;

    private BigDecimal promotionalPrice;

    private int status;

    private Bill bill;

    private ProductDetail productDetail;

    private int defectiveProduct;

    private String imagePath;

    private boolean  rated;
}
