package com.example.demo.service.thuong;

import com.example.demo.entity.Image;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import java.nio.file.Path;
import java.util.List;

public interface ImageServiceTH {
    List<Image> findAllByProduct_Id(Long id);

    void init();

    Path load(String filename);

    Resource loadAsResource(String filename);


    String store(MultipartFile file);
}
