package com.example.demo.service.Customer;

import com.example.demo.entity.Image;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ImageService {

    List<Image> getAll();

    Page<Image> getAll(Integer page);

    Image getById(Long id);

    Image save(Image image);

    List<Image> saveAll(List<Image> images, ProductDetail productDetail);

    Image update(Image image, Long id);

    void delete(Long id);

    List<Image> getByPDid(Long id);
}
