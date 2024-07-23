package com.example.demo.restController.nguyen;

import com.example.demo.entity.Image;
import com.example.demo.service.nguyen.NProductDetailService;
import com.example.demo.service.nguyen.NProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/product")
public class ProductRestController {

    @Autowired
    private NProductService productService;

    @GetMapping("/maxPrice")
    public Double getMaxPrice() {
        return productService.getMaxPrice().doubleValue();
    }

    @GetMapping("/minPrice")
    public Double getMinPrice() {
        return productService.getMinPrice().doubleValue();
    }

}
