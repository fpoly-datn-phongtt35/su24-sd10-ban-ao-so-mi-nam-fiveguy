package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Image;
import com.example.demo.repository.onlineShop.OLImageRepository2;
import com.example.demo.service.onlineShop.OLImageService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OLImageServiceImpl2 implements OLImageService2 {


    @Autowired
    private OLImageRepository2 imageRepository;


    @Override
    public List<String> getImagesByProductIdAndColorId(Long idProduct, Long idColor) {
        return imageRepository.findPathsByProductIdAndColorId(idProduct, idColor);
    }

    @Override
    public String getImagePathByProductId(Long id) {
        return imageRepository.findFirstImagePathByProductId(id);
    }

}