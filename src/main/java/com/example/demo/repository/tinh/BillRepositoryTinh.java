package com.example.demo.repository.tinh;

import com.example.demo.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface BillRepositoryTinh extends JpaRepository<Bill, Long> {

//    1 Tính số lượng sp và đơn theo day week month year

    //Tổng số dơn thanhf cong
    @Query("select b from Bill b JOIN b.billHistories bls where CAST(b.createdAt AS DATE) = CAST(:day AS DATE) and bls.status = 21")
    List<Bill> tongBillThanhCongDay(Date day);
    @Query("SELECT b FROM Bill b JOIN b.billHistories bls WHERE DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.createdAt) = DATEPART(WEEK, :date) AND bls.status = 21")
    List<Bill> tongBillThanhCongWeek(Date date);
    @Query("select b from Bill b JOIN b.billHistories bls where DATEPART(MONTH, b.createdAt) =  Month(:date) and DATEPART(YEAR, b.createdAt) = YEAR(:date) AND bls.status = 21")
    List<Bill> tongBillThanhCongMonth(Date date);
    @Query("select b from Bill b JOIN b.billHistories bls where DATEPART(YEAR, b.createdAt) = YEAR(:date) AND bls.status = 21")
    List<Bill> tongBillThanhCongYear(Date date);
    @Query("SELECT b FROM Bill b " +
            "JOIN b.billHistories bls " +
            "WHERE b.createdAt BETWEEN :startDate AND :endDate " +
            "AND bls.status = 21")
    List<Bill> tongBillThanhCongOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    //Tổng số dơn  Huy
    @Query("SELECT COUNT(DISTINCT b) FROM Bill b WHERE CAST(b.createdAt AS DATE) = CAST(:date AS DATE) AND (b.status = 5 OR b.status = 6)")
    Long tongBillHuyDay(@Param("date") Date date);
    @Query("SELECT COUNT(DISTINCT b) FROM Bill b JOIN b.billHistories ps WHERE DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.createdAt) = DATEPART(WEEK, :date) AND (b.status = 5 OR b.status = 6)")
    Long tongBillHuyWeek(@Param("date") Date date);
    @Query("SELECT COUNT(DISTINCT b) FROM Bill b JOIN b.billHistories ps WHERE DATEPART(MONTH, b.createdAt) = MONTH(:date) AND DATEPART(YEAR, b.createdAt) = YEAR(:date) AND (b.status = 5 OR b.status = 6)")
    Long tongBillHuyMonth(@Param("date") Date date);
    @Query("SELECT COUNT(DISTINCT b) FROM Bill b JOIN b.billHistories ps WHERE DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, :date) AND (b.status = 5 OR b.status = 6)")
    Long tongBillHuyYear(@Param("date") Date date);
    @Query("SELECT COUNT(DISTINCT b) FROM Bill b JOIN b.billHistories ps WHERE b.createdAt BETWEEN :startDate AND :endDate AND (b.status = 5 OR b.status = 6)")
    Long tongBillHuyOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //Tổng số đơn Trả
    @Query("select b from Bill b JOIN b.billHistories bls where CAST(b.createdAt AS Date) = CAST(:day AS DATE) AND bls.status = 32")
    List<Bill> tongBillTraHangDay(Date day);
    @Query("SELECT b FROM Bill b JOIN b.billHistories bls WHERE DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.createdAt) = DATEPART(WEEK, :date) AND bls.status = 32")
    List<Bill> tongBillTraHangWeek(Date date);
    @Query("select b from Bill b JOIN b.billHistories bls where DATEPART(MONTH, b.createdAt) =  Month(:date) and DATEPART(YEAR, b.createdAt) = YEAR(:date) AND bls.status = 32")
    List<Bill> tongBillTraHangMonth(Date date);
    @Query("select b from Bill b JOIN b.billHistories bls where DATEPART(YEAR, b.createdAt) = YEAR(:date) AND bls.status = 32")
    List<Bill> tongBillTraHangYear(Date date);
    @Query("SELECT b FROM Bill b " +
            "JOIN b.billHistories bls " +
            "WHERE b.createdAt BETWEEN :startDate AND :endDate " +
            "AND bls.status = 32")
    List<Bill> tongBillTraHangOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //Tỏng số lượng bill theo trang thai
    @Query("SELECT b FROM Bill b JOIN b.billHistories ps " +
            "WHERE CAST(b.createdAt AS DATE) = CAST(CURRENT_DATE AS DATE) " +
            "AND b.status = :status")
    List<Bill> tongStatusBillDay(@Param("status") Integer status);

    @Query("SELECT b FROM Bill b JOIN b.billHistories ps " +
            "WHERE DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, CURRENT_DATE) " +
            "AND DATEPART(WEEK, b.createdAt) = DATEPART(WEEK, CURRENT_DATE) " +
            "AND b.status = :status")
    List<Bill> tongStatusBillWeek(@Param("status") Integer status);

    @Query("SELECT b FROM Bill b JOIN b.billHistories ps " +
            "WHERE FUNCTION('MONTH', b.createdAt) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND FUNCTION('YEAR', b.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND b.status = :status")
    List<Bill> tongStatusBillMonth(@Param("status") Integer status);

    @Query("SELECT b FROM Bill b JOIN b.billHistories ps " +
            "WHERE FUNCTION('YEAR', b.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND b.status = :status")
    List<Bill> tongStatusBillYear(@Param("status") Integer status);

    @Query("SELECT b FROM Bill b JOIN b.billHistories ps " +
            "WHERE b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = :status")
    List<Bill> tongStatusBillOption(@Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate,
                                    @Param("status") Integer status);




//Tổng tiền
    @Query(value = "WITH DistinctPaymentStatus AS ( " +
            "    SELECT " +
            "        ps.BillId, " +
            "        ps.PaymentAmount, " +
            "        ps.PaymentType, " +
            "        ps.PaymentDate, " +
            "        ROW_NUMBER() OVER (PARTITION BY ps.BillId ORDER BY ps.Id DESC) AS rn " +
            "    FROM " +
            "        PaymentStatus ps " +
            ") " +
            "SELECT " +
            "    SUM( " +
            "        CASE " +
            "            WHEN dps.PaymentType = 4 THEN " +
            "                b.totalAmountAfterDiscount - dps.PaymentAmount " +
            "            ELSE " +
            "                b.totalAmountAfterDiscount " +
            "        END " +
            "    ) AS totalRevenueAdjusted " +
            "FROM " +
            "    Bills b " +
            "    LEFT JOIN DistinctPaymentStatus dps ON b.id = dps.BillId AND dps.rn = 1 " +
            "WHERE " +
            "    EXISTS ( " +
            "        SELECT 1 " +
            "        FROM BillHistories bh " +
            "        WHERE bh.BillId = b.id " +
            "        AND bh.Status = 21 " +
            "    ) " +
            "    AND (:timePeriod = 'day' AND CAST(b.CreatedAt AS DATE) = CAST(:date AS DATE)) " +
            "    OR (:timePeriod = 'week' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.CreatedAt) = DATEPART(WEEK, :date)) " +
            "    OR (:timePeriod = 'month' AND DATEPART(MONTH, b.CreatedAt) = MONTH(:date) AND DATEPART(YEAR, b.CreatedAt) = YEAR(:date)) " +
            "    OR (:timePeriod = 'year' AND DATEPART(YEAR, b.CreatedAt) = YEAR(:date))",
            nativeQuery = true)
    BigDecimal tongSoTien(@Param("date") Date date, @Param("timePeriod") String timePeriod);


    @Query(value = "WITH DistinctPaymentStatus AS ( " +
            "    SELECT " +
            "        ps.BillId, " +
            "        ps.PaymentAmount, " +
            "        ps.PaymentType, " +
            "        ps.PaymentDate, " +
            "        ROW_NUMBER() OVER (PARTITION BY ps.BillId ORDER BY ps.Id DESC) AS rn " +
            "    FROM " +
            "        PaymentStatus ps " +
            ") " +
            "SELECT " +
            "    SUM( " +
            "        CASE " +
            "            WHEN dps.PaymentType = 4 THEN " +
            "                b.totalAmountAfterDiscount - dps.PaymentAmount " +
            "            ELSE " +
            "                b.totalAmountAfterDiscount " +
            "        END " +
            "    ) AS totalRevenueAdjusted " +
            "FROM " +
            "    Bills b " +
            "    LEFT JOIN DistinctPaymentStatus dps ON b.id = dps.BillId AND dps.rn = 1 " +
            "WHERE " +
            "    EXISTS ( " +
            "        SELECT 1 " +
            "        FROM BillHistories bh " +
            "        WHERE bh.BillId = b.id " +
            "        AND bh.Status = 21 " +
            "    ) " +
            "    AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) " +
            "    AND DATEPART(WEEK, b.CreatedAt) = DATEPART(WEEK, :date)",
            nativeQuery = true)
    BigDecimal calculateTotalRevenueForWeek(@Param("date") Date date);

    @Query(value = "WITH DistinctPaymentStatus AS ( " +
            "    SELECT " +
            "        ps.BillId, " +
            "        ps.PaymentAmount, " +
            "        ps.PaymentType, " +
            "        ps.PaymentDate, " +
            "        ROW_NUMBER() OVER (PARTITION BY ps.BillId ORDER BY ps.Id DESC) AS rn " +
            "    FROM " +
            "        PaymentStatus ps " +
            ") " +
            "SELECT " +
            "    SUM( " +
            "        CASE " +
            "            WHEN dps.PaymentType = 4 THEN " +
            "                b.totalAmountAfterDiscount - dps.PaymentAmount " +
            "            ELSE " +
            "                b.totalAmountAfterDiscount " +
            "        END " +
            "    ) AS totalRevenueAdjusted " +
            "FROM " +
            "    Bills b " +
            "    LEFT JOIN DistinctPaymentStatus dps ON b.id = dps.BillId AND dps.rn = 1 " +
            "WHERE " +
            "    EXISTS ( " +
            "        SELECT 1 " +
            "        FROM BillHistories bh " +
            "        WHERE bh.BillId = b.id " +
            "        AND bh.Status = 21 " +
            "    ) " +
            "    AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) " +
            "    AND DATEPART(MONTH, b.CreatedAt) = DATEPART(MONTH, :date)",
            nativeQuery = true)
    BigDecimal calculateTotalRevenueForMonth(@Param("date") Date date);

    @Query(value = "WITH DistinctPaymentStatus AS ( " +
            "    SELECT " +
            "        ps.BillId, " +
            "        ps.PaymentAmount, " +
            "        ps.PaymentType, " +
            "        ps.PaymentDate, " +
            "        ROW_NUMBER() OVER (PARTITION BY ps.BillId ORDER BY ps.Id DESC) AS rn " +
            "    FROM " +
            "        PaymentStatus ps " +
            ") " +
            "SELECT " +
            "    SUM( " +
            "        CASE " +
            "            WHEN dps.PaymentType = 4 THEN " +
            "                b.totalAmountAfterDiscount - dps.PaymentAmount " +
            "            ELSE " +
            "                b.totalAmountAfterDiscount " +
            "        END " +
            "    ) AS totalRevenueAdjusted " +
            "FROM " +
            "    Bills b " +
            "    LEFT JOIN DistinctPaymentStatus dps ON b.id = dps.BillId AND dps.rn = 1 " +
            "WHERE " +
            "    EXISTS ( " +
            "        SELECT 1 " +
            "        FROM BillHistories bh " +
            "        WHERE bh.BillId = b.id " +
            "        AND bh.Status = 21 " +
            "    ) " +
            "    AND b.CreatedAt BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    BigDecimal tongSoTienOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);






















//    @Query("SELECT COALESCE(SUM(b.totalAmountAfterDiscount), 0) AS totalAmount " +
//            "FROM Bill b " +
//            "JOIN b.paymentStatuses ps " +
//            "JOIN b.billHistories bls " +
//            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
//            "AND ps.customerPaymentStatus = 2 " +
//            "AND bls.status = 21 " +
//            "AND b.status = 21")
//    BigDecimal tongSoTienOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
//@Query(value = "WITH DistinctPaymentStatus AS ( " +
//        "    SELECT " +
//        "        ps.BillId, " +
//        "        ps.PaymentAmount, " +
//        "        ps.PaymentType, " +
//        "        ps.PaymentDate, " +
//        "        ROW_NUMBER() OVER (PARTITION BY ps.BillId ORDER BY ps.Id DESC) AS rn " +
//        "    FROM " +
//        "        PaymentStatus ps " +
//        ") " +
//        "SELECT " +
//        "    SUM( " +
//        "        CASE " +
//        "            WHEN dps.PaymentType = 4 THEN " +
//        "                b.totalAmountAfterDiscount - dps.PaymentAmount " +
//        "            ELSE " +
//        "                b.totalAmountAfterDiscount " +
//        "        END " +
//        "    ) AS totalRevenueAdjusted " +
//        "FROM " +
//        "    Bills b " +
//        "    LEFT JOIN DistinctPaymentStatus dps ON b.id = dps.BillId AND dps.rn = 1 " +
//        "WHERE " +
//        "    EXISTS ( " +
//        "        SELECT 1 " +
//        "        FROM BillHistories bh " +
//        "        WHERE bh.BillId = b.id " +
//        "        AND bh.Status = 21 " +
//        "    ) " +
//        "    AND dps.paymentDate BETWEEN :startDate AND :endDate ", nativeQuery = true)
//    BigDecimal tongSoTienOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Modifying
    @Transactional
    @Query("UPDATE Bill b SET b.status = 4 WHERE b.id = :id")
    void updateBillStatus(Long id);

    @Query("SELECT b FROM Bill b WHERE b.status = 1")
    Page<Bill> getAllBillChoThanhToan(Pageable pageable);
//
//    WITH TotalPurchases AS (
//            SELECT
//                    c.Id AS customer_id,
//            c.FullName AS customer_name,
//            c.Avatar AS customer_avatar,
//            SUM(bd.Quantity) AS total_quantity,
//    SUM(b.TotalAmountAfterDiscount) AS total_amount_after_discount
//    FROM
//    Customers c
//    JOIN
//    Bills b ON c.Id = b.IdCustomer
//            JOIN
//    BillDetails bd ON b.Id = bd.IdBill
//            WHERE
//    EXISTS (
//            SELECT 1
//            FROM BillHistories bh
//            WHERE bh.BillId = b.Id
//            AND bh.Status = 21
//    )
//    AND CONVERT(DATE, b.CreatedAt) = CONVERT(DATE, GETDATE())
//    GROUP BY
//    c.Id, c.FullName, c.Avatar
//),
//
//    ReturnTotals AS (
//            SELECT
//                    c.Id AS customer_id,
//            SUM(ro.Quantity) AS total_return_quantity,
//    SUM(ro.RefundPrice) AS total_return_amount
//    FROM
//    Customers c
//    JOIN
//    Bills b ON c.Id = b.IdCustomer
//            JOIN
//    BillDetails bd ON b.Id = bd.IdBill
//            JOIN
//    ReturnOrders ro ON bd.Id = ro.IdBillDetail
//            WHERE
//    CONVERT(DATE, b.CreatedAt) = CONVERT(DATE, GETDATE())
//    GROUP BY
//    c.Id
//),
//
//    RevenueAdjustment AS (
//            SELECT
//                    b.Id AS bill_id,
//            b.TotalAmountAfterDiscount,
//            COALESCE(SUM(ps.PaymentAmount), 0) AS total_payment_amount
//    FROM
//    Bills b
//    LEFT JOIN
//    PaymentStatus ps ON b.Id = ps.BillId AND ps.PaymentType = 4
//    WHERE
//    CONVERT(DATE, b.CreatedAt) = CONVERT(DATE, GETDATE())
//    GROUP BY
//    b.Id, b.TotalAmountAfterDiscount
//)
//
//    SELECT
//    tp.customer_id,
//    tp.customer_name,
//    tp.customer_avatar,
//    tp.total_quantity - COALESCE(rt.total_return_quantity, 0) AS adjusted_quantity,
//    SUM(
//            ra.TotalAmountAfterDiscount
//        - COALESCE(rt.total_return_amount, 0)
//        - COALESCE(ra.total_payment_amount, 0)
//    ) AS adjusted_revenue
//    FROM
//    TotalPurchases tp
//    LEFT JOIN
//    ReturnTotals rt ON tp.customer_id = rt.customer_id
//    LEFT JOIN
//    Bills b ON tp.customer_id = b.IdCustomer
//    LEFT JOIN
//    RevenueAdjustment ra ON b.Id = ra.bill_id
//    GROUP BY
//    tp.customer_id, tp.customer_name, tp.customer_avatar, tp.total_quantity, rt.total_return_quantity
//    ORDER BY
//    adjusted_quantity DESC;



//    Top khách hàng mua nhiều nhất

    @Query(value = """
    WITH TotalPurchases AS (
        SELECT
            c.Id AS customer_id,
            c.FullName AS customer_name,
            c.Avatar AS customer_avatar,
            SUM(bd.Quantity) AS total_quantity,
            SUM(b.TotalAmountAfterDiscount) AS total_amount_after_discount
        FROM
            Customers c
        JOIN
            Bills b ON c.Id = b.IdCustomer
        JOIN
            BillDetails bd ON b.Id = bd.IdBill
        WHERE
            EXISTS (
                SELECT 1
                FROM BillHistories bh
                WHERE bh.BillId = b.Id
                AND bh.Status = 21
            )
            AND (
                (:period = 'day' AND CAST(b.CreatedAt AS DATE) = CAST(:date AS DATE)) OR 
                (:period = 'week' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.CreatedAt) = DATEPART(WEEK, :date)) OR 
                (:period = 'month' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(MONTH, b.CreatedAt) = DATEPART(MONTH, :date)) OR 
                (:period = 'year' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date))
            )
        GROUP BY
            c.Id, c.FullName, c.Avatar
    ),
    
    ReturnTotals AS (
        SELECT
            c.Id AS customer_id,
            SUM(ro.Quantity) AS total_return_quantity,
            SUM(ro.RefundPrice) AS total_return_amount
        FROM
            Customers c
        JOIN
            Bills b ON c.Id = b.IdCustomer
        JOIN
            BillDetails bd ON b.Id = bd.IdBill
        JOIN
            ReturnOrders ro ON bd.Id = ro.IdBillDetail
        WHERE
            (:period = 'day' AND CAST(b.CreatedAt AS DATE) = CAST(:date AS DATE)) OR 
            (:period = 'week' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.CreatedAt) = DATEPART(WEEK, :date)) OR 
            (:period = 'month' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(MONTH, b.CreatedAt) = DATEPART(MONTH, :date)) OR 
            (:period = 'year' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date))
        GROUP BY
            c.Id
    ),
    
    RevenueAdjustment AS (
        SELECT
            b.Id AS bill_id,
            b.TotalAmountAfterDiscount,
            COALESCE(SUM(ps.PaymentAmount), 0) AS total_payment_amount
        FROM
            Bills b
        LEFT JOIN
            PaymentStatus ps ON b.Id = ps.BillId AND ps.PaymentType = 4
        WHERE
            (:period = 'day' AND CAST(b.CreatedAt AS DATE) = CAST(:date AS DATE)) OR 
            (:period = 'week' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.CreatedAt) = DATEPART(WEEK, :date)) OR 
            (:period = 'month' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(MONTH, b.CreatedAt) = DATEPART(MONTH, :date)) OR 
            (:period = 'year' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date))
        GROUP BY
            b.Id, b.TotalAmountAfterDiscount
    )
    
    SELECT
        tp.customer_id,
        tp.customer_name,
        tp.customer_avatar,
        tp.total_quantity - COALESCE(rt.total_return_quantity, 0) AS adjusted_quantity,
        SUM(
            ra.TotalAmountAfterDiscount
            - COALESCE(rt.total_return_amount, 0)
            - COALESCE(ra.total_payment_amount, 0)
        ) AS adjusted_revenue
    FROM
        TotalPurchases tp
    LEFT JOIN
        ReturnTotals rt ON tp.customer_id = rt.customer_id
    LEFT JOIN
        Bills b ON tp.customer_id = b.IdCustomer
    LEFT JOIN
        RevenueAdjustment ra ON b.Id = ra.bill_id
    GROUP BY
        tp.customer_id, tp.customer_name, tp.customer_avatar, tp.total_quantity, rt.total_return_quantity
    ORDER BY
        adjusted_quantity DESC
    """,
            nativeQuery = true)
    List<Map<String, Object>> findKhachHangMuaNhieuNhat(
            @Param("date") Date date,
            @Param("period") String period
    );


    @Query(value = """
    WITH TotalPurchases AS (
        SELECT
            c.Id AS customer_id,
            c.FullName AS customer_name,
            c.Avatar AS customer_avatar,
            SUM(bd.Quantity) AS total_quantity,
            SUM(b.TotalAmountAfterDiscount) AS total_amount_after_discount
        FROM
            Customers c
        JOIN
            Bills b ON c.Id = b.IdCustomer
        JOIN
            BillDetails bd ON b.Id = bd.IdBill
        WHERE
            EXISTS (
                SELECT 1
                FROM BillHistories bh
                WHERE bh.BillId = b.Id
                AND bh.Status = 21
            )
            AND b.CreatedAt BETWEEN :startDate AND :endDate
        GROUP BY
            c.Id, c.FullName, c.Avatar
    ),
    
    ReturnTotals AS (
        SELECT
            c.Id AS customer_id,
            SUM(ro.Quantity) AS total_return_quantity,
            SUM(ro.RefundPrice) AS total_return_amount
        FROM
            Customers c
        JOIN
            Bills b ON c.Id = b.IdCustomer
        JOIN
            BillDetails bd ON b.Id = bd.IdBill
        JOIN
            ReturnOrders ro ON bd.Id = ro.IdBillDetail
        WHERE
            b.CreatedAt BETWEEN :startDate AND :endDate
        GROUP BY
            c.Id
    ),
    
    RevenueAdjustment AS (
        SELECT
            b.Id AS bill_id,
            b.TotalAmountAfterDiscount,
            COALESCE(SUM(ps.PaymentAmount), 0) AS total_payment_amount
        FROM
            Bills b
        LEFT JOIN
            PaymentStatus ps ON b.Id = ps.BillId AND ps.PaymentType = 4
        WHERE
            b.CreatedAt BETWEEN :startDate AND :endDate
        GROUP BY
            b.Id, b.TotalAmountAfterDiscount
    )
    
    SELECT
        tp.customer_id,
        tp.customer_name,
        tp.customer_avatar,
        tp.total_quantity - COALESCE(rt.total_return_quantity, 0) AS adjusted_quantity,
        SUM(
            ra.TotalAmountAfterDiscount
            - COALESCE(rt.total_return_amount, 0)
            - COALESCE(ra.total_payment_amount, 0)
        ) AS adjusted_revenue
    FROM
        TotalPurchases tp
    LEFT JOIN
        ReturnTotals rt ON tp.customer_id = rt.customer_id
    LEFT JOIN
        Bills b ON tp.customer_id = b.IdCustomer
    LEFT JOIN
        RevenueAdjustment ra ON b.Id = ra.bill_id
    GROUP BY
        tp.customer_id, tp.customer_name, tp.customer_avatar, tp.total_quantity, rt.total_return_quantity
    ORDER BY
        adjusted_quantity DESC
    """,
            nativeQuery = true)
    List<Map<String, Object>> findKhachHangMuaNhieuNhatTuyChon(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );




//    Danh sách sản phẩm bán chạy
    @Query(value = """
        SELECT 
            sp.Id AS product_id,
            sp.Name AS ten_sanpham,
            COALESCE(SUM(bd.Quantity) - COALESCE(SUM(CASE WHEN ps.PaymentType = 4 THEN ro.Quantity ELSE 0 END), 0), 0) AS so_luong_ban,
            sp.Price AS price
        FROM 
            Products sp
        JOIN 
            ProductDetails pd ON sp.Id = pd.IdProduct
        JOIN 
            BillDetails bd ON pd.Id = bd.IdProductDetail
        JOIN 
            Bills b ON bd.IdBill = b.Id
        LEFT JOIN 
            ReturnOrders ro ON bd.Id = ro.IdBillDetail
        LEFT JOIN 
            PaymentStatus ps ON b.Id = ps.BillId AND ps.PaymentType = 4
        WHERE 
            EXISTS (
                SELECT 1
                FROM BillHistories bh
                WHERE bh.BillId = b.Id
                AND bh.Status = 21
            )
            AND (
                (:period = 'day' AND CAST(b.CreatedAt AS DATE) = CAST(:date AS DATE)) OR 
                (:period = 'week' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.CreatedAt) = DATEPART(WEEK, :date)) OR 
                (:period = 'month' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date) AND DATEPART(MONTH, b.CreatedAt) = DATEPART(MONTH, :date)) OR 
                (:period = 'year' AND DATEPART(YEAR, b.CreatedAt) = DATEPART(YEAR, :date))
            )
        GROUP BY 
            sp.Id, sp.Name, sp.Price
        ORDER BY 
            so_luong_ban DESC, sp.Price DESC
    """, nativeQuery = true)
    List<Object[]> findSanPhamBanChay(@Param("date") Date date, @Param("period") String period);


    @Query(value = """
    SELECT 
        sp.Id AS product_id,
        sp.Name AS ten_sanpham,
        COALESCE(SUM(bd.Quantity) - COALESCE(SUM(CASE WHEN ps.PaymentType = 4 THEN ro.Quantity ELSE 0 END), 0), 0) AS so_luong_ban,
        sp.Price AS price
    FROM 
        Products sp
    JOIN 
        ProductDetails pd ON sp.Id = pd.IdProduct
    JOIN 
        BillDetails bd ON pd.Id = bd.IdProductDetail
    JOIN 
        Bills b ON bd.IdBill = b.Id
    LEFT JOIN 
        ReturnOrders ro ON bd.Id = ro.IdBillDetail
    LEFT JOIN 
        PaymentStatus ps ON b.Id = ps.BillId AND ps.PaymentType = 4
    WHERE 
        EXISTS (
            SELECT 1
            FROM BillHistories bh
            WHERE bh.BillId = b.Id
            AND bh.Status = 21
        )
        AND b.CreatedAt BETWEEN :startDate AND :endDate
    GROUP BY 
        sp.Id, sp.Name, sp.Price
    ORDER BY 
        so_luong_ban DESC, sp.Price DESC
""", nativeQuery = true)
    List<Object[]> findSanPhamBanChayTuyChon(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
