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
    private Float rate;
    private Integer totalRate;

    public ProductDetailsDTO(ProductInfoDTO productInfo, List<Object[]> colors, List<Object[]> sizes, List<String> images, Integer quantity, Float rate, Integer totalRate) {
        this.productInfo = productInfo;
        this.colors = colors;
        this.sizes = sizes;
        this.images = images;
        this.quantity = quantity;
        this.rate = rate;
        this.totalRate = totalRate;
    }
}