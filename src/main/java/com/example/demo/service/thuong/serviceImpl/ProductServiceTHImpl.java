package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.model.request.thuong.ProductRequestTH;
import com.example.demo.repository.thuong.ImageRepositoryTH;
import com.example.demo.repository.thuong.ProductDetailRepositoryTH;
import com.example.demo.repository.thuong.ProductRepositoryTH;
import com.example.demo.service.ImageCloud;
import com.example.demo.service.thuong.ProductServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceTHImpl implements ProductServiceTH {
    @Autowired
    private ProductRepositoryTH productRepository;

    @Autowired
    private ProductDetailRepositoryTH productDetailRepository;

    @Autowired
    private ImageCloud imageCloud;

    @Autowired
    private ImageRepositoryTH imageRepository;

    @Override
    public Product create(ProductRequestTH productRequest) throws IOException {
        Product product = new Product();
        product.setCode(productRequest.getCode());
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescribe(productRequest.getDescribe());
        product.setCreatedAt(new Date());
        product.setCreatedBy(productRequest.getCreatedBy());
        product.setCategory(productRequest.getCategory());
        product.setSupplier(productRequest.getSupplier());
        product.setMaterial(productRequest.getMaterial());
        product.setWrist(productRequest.getWrist());
        product.setCollar(productRequest.getCollar());

        Product saveProduct = productRepository.save(product);
        productRequest.getProductDetails().forEach(ctsp -> ctsp.setProduct(saveProduct));
        productDetailRepository.saveAll(product.getProductDetails());
        List<Image> images = new ArrayList<>();
        for (Image img : productRequest.getImages()) {
            Image image = new Image();
            image.setName(img.getName());
            image.setPath(img.getPath());
            image.setCreatedAt(new Date());
            image.setStatus(img.getStatus());
            image.setProduct(saveProduct);
            image.setColor(img.getColor());
            images.add(image);
        }
        imageRepository.saveAll(images);
        return saveProduct;
    }
}
