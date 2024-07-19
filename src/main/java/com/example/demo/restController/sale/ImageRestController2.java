package com.example.demo.restController.sale;

import com.example.demo.service.sale.BrandService2;
import com.example.demo.service.sale.ImageService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/image")
public class ImageRestController2 {



    @Autowired
    private ImageService2 imageService2 ;

    @GetMapping("/firstImagePath")
    public String getFirstImagePathByProductId(@RequestParam String productId) {
        return imageService2.findImagesByProductId(Long.parseLong(productId));
    }

}
