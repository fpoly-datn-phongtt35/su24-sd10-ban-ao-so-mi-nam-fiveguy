package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Sale;
import com.example.demo.repository.onlineShop.OLSaleRepository2;
import com.example.demo.service.onlineShop.OLSaleService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class OLSaleServiceImpl2 implements OLSaleService2 {
    @Autowired
    private OLSaleRepository2 saleRepository;

    @Override
    public List<String> findAllPathsByStatus() {
        List<String> paths = saleRepository.findAllPathsByStatus();
        // Loại bỏ các giá trị null từ danh sách
        paths.removeIf(Objects::isNull);
        return paths;
    }




}
