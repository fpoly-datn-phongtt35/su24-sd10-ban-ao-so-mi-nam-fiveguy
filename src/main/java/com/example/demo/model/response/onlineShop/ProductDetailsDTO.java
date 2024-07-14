package com.example.demo.model.response.onlineShop;

import lombok.Data;

import java.util.List;
@Data

public class ProductDetailsDTO {
    private ProductInfoDTO productInfo;
    private List<Object[]> colors;
    private List<Object[]> sizes;
    private List<String> images;
    private Integer quantity;

    // constructors, getters, and setters


    public ProductDetailsDTO(ProductInfoDTO productInfo, List<Object[]> colors, List<Object[]> sizes, List<String> images, Integer quantity) {
        this.productInfo = productInfo;
        this.colors = colors;
        this.sizes = sizes;
        this.images = images;
        this.quantity = quantity;
    }
}