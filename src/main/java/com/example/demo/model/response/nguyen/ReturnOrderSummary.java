package com.example.demo.model.response.nguyen;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReturnOrderSummary {

    //Chưa trả
    private BigDecimal tongTien;
    private BigDecimal tongTienDaGiam;

    //Sau khi trả
    private BigDecimal tongTienSauKhiTra;
    private BigDecimal tongTienDaGiamSauKhiTra;

    //Tỷ lệ giảm = totalAmountAfterDiscount / totalAmount
    private BigDecimal tiLeGiam;

    //Số lượng billDetail & returnOrder
    private int tongSoLuong;
    private int tongSoLuongTraLai;

    //Giá trị trung bình của sản phẩm
    private BigDecimal giaTrungBinhSanPham;
    private BigDecimal giaTrungBinhSanPhamDaGiam;

    //Tổng số tiền trả
    private BigDecimal tongTienTra;
}
