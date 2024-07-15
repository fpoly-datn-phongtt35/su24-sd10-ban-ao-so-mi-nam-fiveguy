package com.example.demo.service.thuong;

import com.example.demo.entity.Image;

import java.util.List;

public interface ImageServiceTH {
    List<Image> findAllByProduct_Id (Long id);
}
