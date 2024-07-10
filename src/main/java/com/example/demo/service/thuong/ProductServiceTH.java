package com.example.demo.service.thuong;

import com.example.demo.entity.Product;
import com.example.demo.model.request.thuong.ProductRequestTH;

import java.io.IOException;

public interface ProductServiceTH {
    Product create(ProductRequestTH productRequestTH) throws IOException;
}
