package com.example.demo.service.thuong;

import com.example.demo.model.response.thuong.CustomerResponseTH;

public interface CustomerServiceTH {
    CustomerResponseTH create(CustomerResponseTH customer, String name);
}
