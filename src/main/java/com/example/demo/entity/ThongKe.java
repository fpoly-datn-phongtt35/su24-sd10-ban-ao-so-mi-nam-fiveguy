package com.example.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongKe {
    private Long sanpham_id;

    private String ten_sanpham;

    private BigDecimal price;

    private int so_luong_ban;

    private BigDecimal doanh_thu;

    private String anh_mac_dinh;

}
