package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ThongKeKhachHang {
    private Long khachhangId;
    private String tenKhachHang;
    private Integer tongSoLuongMua;
    private BigDecimal tongDoanhThu;
    private Integer tongSoBill; // Thêm thuộc tính này
}
