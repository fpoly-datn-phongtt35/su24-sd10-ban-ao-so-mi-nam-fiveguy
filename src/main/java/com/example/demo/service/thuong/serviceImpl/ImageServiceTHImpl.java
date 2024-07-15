package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.Image;
import com.example.demo.repository.thuong.ImageRepositoryTH;
import com.example.demo.service.thuong.ImageServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceTHImpl implements ImageServiceTH {
    @Autowired
    private ImageRepositoryTH repository;
    @Override
    public List<Image> findAllByProduct_Id(Long id) {
        return repository.findAllByProduct_Id(id);
    }
}
