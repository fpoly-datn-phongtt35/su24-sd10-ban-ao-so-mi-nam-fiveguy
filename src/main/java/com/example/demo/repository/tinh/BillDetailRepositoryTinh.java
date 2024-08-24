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
    @Query(value = "SELECT SUM(bd.quantity - COALESCE(ro.returnQuantity, 0)) AS totalQuantitySold " +
            "FROM BillDetails bd " +
            "JOIN Bills b ON bd.IdBill = b.id " +
            "LEFT JOIN ( " +
            "    SELECT r.IdBillDetail, " +
            "           SUM(r.Quantity) AS returnQuantity " +
            "    FROM ReturnOrders r " +
            "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
            "    WHERE p.PaymentType = 4 " +
            "    GROUP BY r.IdBillDetail " +
            ") ro ON bd.id = ro.IdBillDetail " +
            "WHERE EXISTS ( " +
            "    SELECT 1 " +
            "    FROM BillHistories bh " +
            "    WHERE bh.BillId = b.id " +
            "    AND bh.Status = 21 " +
            ") " +
            "AND CAST(b.createdAt AS DATE) = :date",
            nativeQuery = true)
    Integer sanPhamBanDuocNgay(@Param("date") Date  date);

//    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
//            "FROM BillDetail hdct " +
//            "JOIN hdct.bill b " +
//            "JOIN b.paymentStatuses ps " +
//            "JOIN b.billHistories bls " +
//            "WHERE CAST(ps.paymentDate AS DATE) = CAST(:date AS DATE) AND ps.customerPaymentStatus = 2 AND b.status = 21 And bls.status = 21")
//    Integer sanPhamBanDuocNgay(Date date);
@Query(value = "SELECT SUM(bd.quantity - COALESCE(ro.returnQuantity, 0)) AS totalQuantitySold " +
        "FROM BillDetails bd " +
        "JOIN Bills b ON bd.IdBill = b.id " +
        "LEFT JOIN ( " +
        "    SELECT r.IdBillDetail, " +
        "           SUM(r.Quantity) AS returnQuantity " +
        "    FROM ReturnOrders r " +
        "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
        "    WHERE p.PaymentType = 4 " +
        "    GROUP BY r.IdBillDetail " +
        ") ro ON bd.id = ro.IdBillDetail " +
        "WHERE EXISTS ( " +
        "    SELECT 1 " +
        "    FROM BillHistories bh " +
        "    WHERE bh.BillId = b.id " +
        "    AND bh.Status = 21 " +
        ") " +
        "AND DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.createdAt) = DATEPART(WEEK, :date)",
        nativeQuery = true)
Integer sanPhamBanDuocWeed(@Param("date") Date  date);

//    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
//            "FROM BillDetail hdct " +
//            "JOIN hdct.bill b " +
//            "JOIN b.paymentStatuses ps " +
//            "JOIN b.billHistories bls " +
//            "WHERE DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, :date) " +
//            "AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :date) " +
//            "AND ps.customerPaymentStatus = 2 AND b.status = 21 AND bls.status = 21")
//    Integer sanPhamBanDuocWeed(Date date);

    @Query(value = "SELECT SUM(bd.quantity - COALESCE(ro.returnQuantity, 0)) AS totalQuantitySold " +
            "FROM BillDetails bd " +
            "JOIN Bills b ON bd.IdBill = b.id " +
            "LEFT JOIN ( " +
            "    SELECT r.IdBillDetail, " +
            "           SUM(r.Quantity) AS returnQuantity " +
            "    FROM ReturnOrders r " +
            "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
            "    WHERE p.PaymentType = 4 " +
            "    GROUP BY r.IdBillDetail " +
            ") ro ON bd.id = ro.IdBillDetail " +
            "WHERE EXISTS ( " +
            "    SELECT 1 " +
            "    FROM BillHistories bh " +
            "    WHERE bh.BillId = b.id " +
            "    AND bh.Status = 21 " +
            ") " +
            "AND DATEPART(MONTH, b.createdAt) = MONTH(:date) AND DATEPART(YEAR, b.createdAt) = YEAR(:date)",
            nativeQuery = true)
    Integer sanPhamBanDuocMonth(@Param("date") Date  date);
//    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
//            "FROM BillDetail hdct " +
//            "JOIN hdct.bill b " +
//            "JOIN b.paymentStatuses ps " +
//            "JOIN b.billHistories bls " +
//            "WHERE DATEPART(MONTH, ps.paymentDate) = MONTH(:date) " +
//            "AND DATEPART(YEAR, ps.paymentDate) = YEAR(:date) " +
//            "AND ps.customerPaymentStatus = 2 AND b.status = 21 AND bls.status = 21")
//    Integer sanPhamBanDuocMonth(Date date);

    @Query(value = "SELECT SUM(bd.quantity - COALESCE(ro.returnQuantity, 0)) AS totalQuantitySold " +
            "FROM BillDetails bd " +
            "JOIN Bills b ON bd.IdBill = b.id " +
            "LEFT JOIN ( " +
            "    SELECT r.IdBillDetail, " +
            "           SUM(r.Quantity) AS returnQuantity " +
            "    FROM ReturnOrders r " +
            "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
            "    WHERE p.PaymentType = 4 " +
            "    GROUP BY r.IdBillDetail " +
            ") ro ON bd.id = ro.IdBillDetail " +
            "WHERE EXISTS ( " +
            "    SELECT 1 " +
            "    FROM BillHistories bh " +
            "    WHERE bh.BillId = b.id " +
            "    AND bh.Status = 21 " +
            ") " +
            "AND DATEPART(YEAR, b.createdAt) = YEAR(:date)",
            nativeQuery = true)
    Integer sanPhamBanDuocNam(@Param("date") Date  date);
//    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
//            "FROM BillDetail hdct " +
//            "JOIN hdct.bill b " +
//            "JOIN b.paymentStatuses ps " +
//            "JOIN b.billHistories bls " +
//            "WHERE DATEPART(YEAR, ps.paymentDate) = YEAR(:date) " +
//            "AND ps.customerPaymentStatus = 2 AND b.status = 21 AND bls.status = 21")
//    Integer sanPhamBanDuocNam(Date date);
@Query(value = "SELECT SUM(bd.quantity - COALESCE(ro.returnQuantity, 0)) AS totalQuantitySold " +
        "FROM BillDetails bd " +
        "JOIN Bills b ON bd.IdBill = b.id " +
        "LEFT JOIN ( " +
        "    SELECT r.IdBillDetail, " +
        "           SUM(r.Quantity) AS returnQuantity " +
        "    FROM ReturnOrders r " +
        "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
        "    WHERE p.PaymentType = 4 " +
        "    GROUP BY r.IdBillDetail " +
        ") ro ON bd.id = ro.IdBillDetail " +
        "WHERE EXISTS ( " +
        "    SELECT 1 " +
        "    FROM BillHistories bh " +
        "    WHERE bh.BillId = b.id " +
        "    AND bh.Status = 21 " +
        ") " +
        "AND b.createdAt BETWEEN :startDate AND :endDate",
        nativeQuery = true)
Integer sanPhamBanDuocTuyChinh(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

//    @Query("SELECT COALESCE(SUM(hdct.quantity), 0) " +
//            "FROM BillDetail hdct " +
//            "JOIN hdct.bill b " +
//            "JOIN b.paymentStatuses ps " +
//            "JOIN b.billHistories bls " +
//            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
//            "AND ps.customerPaymentStatus = 2 " +
//            "AND b.status = 21 AND bls.status = 21")
//    Integer sanPhamBanDuocTuyChinh(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
