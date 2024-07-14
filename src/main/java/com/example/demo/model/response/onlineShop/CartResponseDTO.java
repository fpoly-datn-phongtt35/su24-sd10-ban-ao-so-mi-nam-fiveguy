package com.example.demo.model.response.onlineShop;

import com.example.demo.entity.Cart;
import com.example.demo.entity.ProductDetail;
import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartResponseDTO {

    private Long id;

    private int quantity;

    private BigDecimal price;

    private Integer promotionalPrice;

    private Cart cart;

    private String name;

    private String nameColor;

    private String nameSize;

    private String path;

    private ProductDetail productDetail;

}