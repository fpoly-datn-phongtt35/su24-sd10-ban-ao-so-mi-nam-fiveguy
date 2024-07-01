package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartDetail;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.onlineShop.OLCartRepository2;
import com.example.demo.service.onlineShop.OLCartService2;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class OLCartServiceImpl2 implements OLCartService2 {

//    @Autowired
//    private OlAccountService olAccountService;

    @Autowired
    private OLCartRepository2 olCartRepository;

//    @Autowired
//    private OlCustomerService2 olCustomerService;

//    @Autowired
//    private OlCartService olCartService;

//    @Autowired
//    private OlCartDetailService olCartDetailService;
//
//    @Autowired
//    private OLProductDetailService olProductDetailService;

//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @Override
    public Cart findByCustomerId(Long id) {
        return olCartRepository.findByCustomerId(id);
    }

    @Override
    public Cart save(Cart gioHang) {
        return olCartRepository.save(gioHang);
    }


}
