package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Bill;
import com.example.demo.model.response.point.BillWithRevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OLBillRepository2 extends JpaRepository<Bill, Long> {


    @Query("SELECT b FROM Bill b  WHERE b.customer.id = :customerId AND b.code LIKE %:search%")
    Page<Bill> findByCustomer_Id(Long customerId, String search, Pageable pageable);

    @Query("SELECT b FROM Bill b  WHERE b.phoneNumber = :phoneNumber AND  b.code LIKE %:search%")
    Page<Bill> findByPhoneNumber(String phoneNumber, String search, Pageable pageable);


    @Query(value = """
    WITH DistinctPaymentStatus AS (
        SELECT 
            ps.BillId,
            ps.PaymentAmount,
            ps.PaymentType,
            ROW_NUMBER() OVER (PARTITION BY ps.BillId ORDER BY ps.Id DESC) AS rn
        FROM 
            PaymentStatus ps
    )
    SELECT
        SUM(
            CASE 
                WHEN dps.PaymentType = 4 THEN 
                    b.totalAmountAfterDiscount - dps.PaymentAmount
                ELSE 
                    b.totalAmountAfterDiscount 
            END
        ) AS totalRevenueAdjusted
    FROM
        Bills b
        LEFT JOIN DistinctPaymentStatus dps ON b.id = dps.BillId AND dps.rn = 1
    WHERE
        b.id = :id
        AND EXISTS (
            SELECT 1
            FROM BillHistories bh
            WHERE bh.BillId = b.id
            AND bh.Status = 21
        )
    GROUP BY b.id
""", nativeQuery = true)
    BigDecimal findBillWithRevenueById(@Param("id") Long id);

}


