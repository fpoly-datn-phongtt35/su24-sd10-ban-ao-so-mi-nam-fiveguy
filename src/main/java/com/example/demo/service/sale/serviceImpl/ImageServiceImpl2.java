package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Image;
import com.example.demo.repository.onlineShop.OLImageRepository2;
import com.example.demo.repository.sale.ImageRepository2;
import com.example.demo.service.onlineShop.OLImageService2;
import com.example.demo.service.sale.ImageService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl2 implements ImageService2 {


    @Autowired
    private ImageRepository2 imageRepository;


    @Override
    public String findImagesByProductId(Long productId) {
        List<String> paths = imageRepository.findImagePathsByProductId(productId);
        return paths.isEmpty() ? null : paths.get(0);
    }

}