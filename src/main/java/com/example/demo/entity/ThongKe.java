package com.example.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class ThongKe {
    private Long idSanPham;
    private String tenSanPham;
    private String image;
    private int soLuongBan;
    private BigDecimal price;

    // Constructor

    public ThongKe(Long idSanPham, String tenSanPham, String image, int soLuongBan, BigDecimal price) {
        this.idSanPham = idSanPham;
        this.tenSanPham = tenSanPham;
        this.image = image;
        this.soLuongBan = soLuongBan;
        this.price = price;
    }


    // Getters and setters
}
