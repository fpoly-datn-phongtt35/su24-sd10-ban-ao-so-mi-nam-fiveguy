package com.example.demo.repository.tinh;

import com.example.demo.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BillDetailRepositoryTinh extends JpaRepository<BillDetail, Long> {
    //Tổng số sản phẩm
    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
            "FROM BillDetail hdct " +
            "JOIN hdct.bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE CAST(ps.paymentDate AS DATE) = CAST(:date AS DATE) AND ps.customerPaymentStatus = 2 AND b.status = 21 And bls.status = 21")
    Integer sanPhamBanDuocNgay(Date date);

    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
            "FROM BillDetail hdct " +
            "JOIN hdct.bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, :date) " +
            "AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :date) " +
            "AND ps.customerPaymentStatus = 2 AND b.status = 21 AND bls.status = 21")
    Integer sanPhamBanDuocWeed(Date date);

    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
            "FROM BillDetail hdct " +
            "JOIN hdct.bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE DATEPART(MONTH, ps.paymentDate) = MONTH(:date) " +
            "AND DATEPART(YEAR, ps.paymentDate) = YEAR(:date) " +
            "AND ps.customerPaymentStatus = 2 AND b.status = 21 AND bls.status = 21")
    Integer sanPhamBanDuocMonth(Date date);

    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
            "FROM BillDetail hdct " +
            "JOIN hdct.bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE DATEPART(YEAR, ps.paymentDate) = YEAR(:date) " +
            "AND ps.customerPaymentStatus = 2 AND b.status = 21 AND bls.status = 21")
    Integer sanPhamBanDuocNam(Date date);

    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
            "FROM BillDetail hdct " +
            "JOIN hdct.bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
            "AND ps.customerPaymentStatus = 2 " +
            "AND b.status = 21 AND bls.status = 21")
    Integer sanPhamBanDuocTuyChinh(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
