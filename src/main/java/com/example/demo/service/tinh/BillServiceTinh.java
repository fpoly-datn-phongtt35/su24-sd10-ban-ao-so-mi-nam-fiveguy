package com.example.demo.service.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.ThongKe;

import java.util.Date;
import java.util.List;

public interface BillServiceTinh {
//    List<Bill> getAll();

    Bill create(Bill bill);

    void updateBillStatus(Long id);

    List<ThongKe> getSanPhamBanChayNgay(Date date);

    List<ThongKe> getSanPhamBanChayTuan(Date date);

    List<ThongKe> getSanPhamBanChayThang(Date date);

    List<ThongKe> getSanPhamBanChayNam(Date date);
}
