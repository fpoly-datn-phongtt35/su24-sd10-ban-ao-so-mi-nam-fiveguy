package com.example.demo.service.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.ThongKe;
import com.example.demo.entity.ThongKeKhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface BillServiceTinh {
//    List<Bill> getAll();

    Bill create(Bill bill);

    void updateBillStatus(Long id);

    Page<ThongKe> getSanPhamBanChayNgay(Date date, Pageable pageable);

    Page<ThongKe> getSanPhamBanChayTuan(Date date, Pageable pageable);

    Page<ThongKe> getSanPhamBanChayThang(Date date, Pageable pageable);

    Page<ThongKe> getSanPhamBanChayNam(Date date, Pageable pageable);

    Page<ThongKe> getSanPhamBanChayTrongKhoangThoiGian(Date startDate, Date endDate, Pageable pageable);

    Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNgay(Date date, Pageable pageable);

    Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuan(Date date, Pageable pageable);

    Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatThang(Date date, Pageable pageable);

    Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNam(Date date, Pageable pageable);


    Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuyChinh(Date startDate, Date endDate, Pageable pageable);



}
