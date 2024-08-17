package com.example.demo.model.response.onlineShop;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponse2 {

    private int status;
    private String productName;
    private int availableQuantity;

    // Constructors, getters, setters

    public CartResponse2(int status, String productName, int availableQuantity) {
        this.status = status;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
    }




}
