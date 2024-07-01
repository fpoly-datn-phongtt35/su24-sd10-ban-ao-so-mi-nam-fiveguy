package com.example.demo.service.onlineShop;

import com.example.demo.entity.Cart;
import com.fasterxml.jackson.databind.JsonNode;

public interface OLCartService2 {


    Cart findByCustomerId(Long id);

    Cart save(Cart gioHang);


//    Object saveAllProductDetail(JsonNode orderData);
}
