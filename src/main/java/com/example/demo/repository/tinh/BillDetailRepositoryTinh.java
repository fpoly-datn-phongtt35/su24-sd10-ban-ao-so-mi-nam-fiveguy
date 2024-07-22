package com.example.demo.repository.tinh;

import com.example.demo.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BillDetailRepositoryTinh extends JpaRepository<BillDetail, Long> {
    //Tổng số sản phẩm
    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) from BillDetail hdct where hdct.bill.paymentDate  = :date and hdct.bill.status=4")
    Integer sanPhamBanDuocNgay(Date date);
    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) FROM BillDetail hdct WHERE DATEPART(YEAR, hdct.bill.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, hdct.bill.paymentDate) = DATEPART(WEEK, :date) AND hdct.bill.status = 4")
    Integer sanPhamBanDuocWeed(Date date);
    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) from BillDetail hdct where DATEPART(MONTH, hdct.bill.paymentDate) =  Month(:date) and DATEPART(YEAR, hdct.bill.paymentDate) = YEAR(:date) and hdct.bill.status=4")
    Integer sanPhamBanDuocMonth(Date date);
    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) from BillDetail hdct where DATEPART(YEAR, hdct.bill.paymentDate) = YEAR(:date) and hdct.bill.status=4")
    Integer sanPhamBanDuocNam(Date date);
}
