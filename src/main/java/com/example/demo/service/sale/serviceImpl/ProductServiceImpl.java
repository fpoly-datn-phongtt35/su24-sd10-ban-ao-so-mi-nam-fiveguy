package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Product;
import com.example.demo.repository.sale.ProductRepository;
import com.example.demo.service.sale.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getProductsWithoutSaleOrExpiredPromotion() {
        Date currentDate = new Date();
        List<Product> productsWithoutSale = productRepository.findByProductSalesIsNull();
        List<Product> productsWithExpiredPromotion = productRepository.findByProductSalesEndDateBefore(currentDate);
        List<Product> mergedProducts = new ArrayList<>(productsWithoutSale);
        mergedProducts.addAll(productsWithExpiredPromotion);
        return mergedProducts;
    }

//    @Override
//    public List<Product> getProductsBySaleId(Long id) {
//        return productRepository.findProductsBySaleId(id);
//    }
}