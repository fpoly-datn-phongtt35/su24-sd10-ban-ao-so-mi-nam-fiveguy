package com.example.demo.service.serviceImpl;

import com.example.demo.entity.ProductSale;
import com.example.demo.repository.ProductSaleRepository;
import com.example.demo.service.ProductSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleServiceImpl implements ProductSaleService {

    @Autowired
    private ProductSaleRepository productSaleRepository;

    @Override
    public ProductSale saveProductSale(ProductSale productSale) {
        return productSaleRepository.save(productSale);
    }

    @Override
    public ProductSale updateProductSale(Long id, ProductSale productSale) {
        Optional<ProductSale> existingProductSale = productSaleRepository.findById(id);
        if (existingProductSale.isPresent()) {
            ProductSale updatedProductSale = existingProductSale.get();
            updatedProductSale.setPromotionalPrice(productSale.getPromotionalPrice());
            updatedProductSale.setDiscountPrice(productSale.getDiscountPrice());
            updatedProductSale.setStatus(productSale.getStatus());
            updatedProductSale.setSale(productSale.getSale());
            updatedProductSale.setProduct(productSale.getProduct());
            return productSaleRepository.save(updatedProductSale);
        } else {
            throw new RuntimeException("ProductSale not found with id " + id);
        }
    }

    @Override
    public void deleteProductSale(Long id) {
        productSaleRepository.deleteById(id);
    }

    @Override
    public ProductSale getProductSaleById(Long id) {
        return productSaleRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductSale not found with id " + id));
    }

    @Override
    public List<ProductSale> getAllProductSales() {
        return productSaleRepository.findAll();
    }
}