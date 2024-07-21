package com.example.demo.service.Customer;

import com.example.demo.entity.Cart;
import com.fasterxml.jackson.databind.JsonNode;

public interface CartServiceH {
// //OL

    Cart findByCustomerId(Long id);

    Cart save(Cart gioHang);

    Object saveAllProductDetail(JsonNode orderData);
// //END OL
}
