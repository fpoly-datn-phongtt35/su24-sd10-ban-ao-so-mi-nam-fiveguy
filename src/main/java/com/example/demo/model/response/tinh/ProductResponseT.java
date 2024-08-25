package com.example.demo.model.response.tinh;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data

public class ProductResponseT {


    private String image;

    private String name;

    private int totalQuantity;

    private BigDecimal price;

    private List<String> sizes;


}
