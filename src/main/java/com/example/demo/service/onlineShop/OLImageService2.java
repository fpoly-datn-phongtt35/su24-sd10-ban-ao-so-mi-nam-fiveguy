package com.example.demo.service.onlineShop;

import com.example.demo.entity.Image;

import java.util.List;

public interface OLImageService2 {
    List<String> getImagesByProductIdAndColorId(Long idProduct, Long idColor);
}
