package com.example.demo.restController.tinh;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.service.tinh.ProductDetailServiceTinh;
import com.example.demo.untility.tinh.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/product-tinh")
public class ProductDetailRestControllerTinh {
    @Autowired
    ProductDetailServiceTinh productServiceTinh;

    @GetMapping("")
    public ResponseEntity<List<ProductDetail>> getAll(){
        List<ProductDetail> products= productServiceTinh.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/page")
    public PaginationResponse<ProductDetail> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) BigDecimal price,

            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5);
        Page<ProductDetail> page = productServiceTinh.findProductDetal(name, code, price, pageable);
        return new PaginationResponse<>(page);
    }
}
