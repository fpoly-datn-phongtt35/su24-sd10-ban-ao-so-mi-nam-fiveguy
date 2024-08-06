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

@Repository
public interface BillRepositoryTinh extends JpaRepository<Bill, Long> {
    //Tống số tiền
    @Query("SELECT COALESCE(SUM(b.totalAmountAfterDiscount), 0) AS totalAmount " +
            "FROM Bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE ps.paymentDate = :date " +
            "AND ps.customerPaymentStatus = 2 " +
            "AND bls.status = 21 " +
            "AND b.status = 21")
    BigDecimal tongSoTienDay(Date date);
    @Query("SELECT COALESCE(SUM(v.totalAmountAfterDiscount), 0) AS totalAmount " +
            "FROM Bill v " +
            "JOIN v.paymentStatuses ps " +
            "JOIN v.billHistories bls " +
            "WHERE DATEPART(YEAR, ps.paymentDate) = YEAR(:date) " +
            "AND ps.customerPaymentStatus = 2 " +
            "AND bls.status = 21 " +
            "AND v.status = 21 " +
            "AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :date)")
    BigDecimal tongSoTienWeek(Date date);

    @Query("SELECT COALESCE(SUM(v.totalAmountAfterDiscount), 0) AS totalAmount FROM Bill v JOIN v.paymentStatuses ps JOIN v.billHistories bls where DATEPART(MONTH, ps.paymentDate) =  Month(:date) and DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and ps.customerPaymentStatus = 2 and v.status=21 AND bls.status = 21")
    BigDecimal tongSoTienMonth(Date date);
    @Query("SELECT COALESCE(SUM(v.totalAmountAfterDiscount), 0) AS totalAmount FROM Bill v JOIN v.paymentStatuses ps JOIN v.billHistories bls where DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and ps.customerPaymentStatus = 2 and v.status=21 AND bls.status = 21")
    BigDecimal tongSoTienYear(Date date);
    @Query("SELECT COALESCE(SUM(b.totalAmountAfterDiscount), 0) AS totalAmount " +
            "FROM Bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
            "AND ps.customerPaymentStatus = 2 " +
            "AND bls.status = 21 " +
            "AND b.status = 21")
    BigDecimal tongSoTienOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    //Tổng số dơn thanhf cong
    @Query("select b from Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls where ps.paymentDate = :day and ps.customerPaymentStatus = 2  and b.status=21 AND bls.status = 21")
    List<Bill> tongBillThanhCongDay(Date day);
    @Query("SELECT b FROM Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls WHERE DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :date) and ps.customerPaymentStatus = 2 AND b.status = 21 AND bls.status = 21")
    List<Bill> tongBillThanhCongWeek(Date date);
    @Query("select b from Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls where DATEPART(MONTH, ps.paymentDate) =  Month(:date) and DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and ps.customerPaymentStatus = 2 and b.status=21 AND bls.status = 21")
    List<Bill> tongBillThanhCongMonth(Date date);
    @Query("select b from Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls where DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and ps.customerPaymentStatus = 2 and b.status=21 AND bls.status = 21")
    List<Bill> tongBillThanhCongYear(Date date);
    @Query("SELECT b FROM Bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
            "AND ps.customerPaymentStatus = 2 " +
            "AND bls.status = 21"+
            "AND b.status = 21")
    List<Bill> tongBillThanhCongOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    //Tổng số dơn  Huy
    @Query("select b from Bill b JOIN b.paymentStatuses ps where ps.paymentDate = :day and b.status= 5 or ps.paymentDate = :day and b.status= 6")
    List<Bill> tongBillHuyDay(Date day);
    @Query("SELECT b FROM Bill b JOIN b.paymentStatuses ps WHERE DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :date) AND b.status=5 or DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :date) and b.status=6")
    List<Bill> tongBillHuyWeek(Date date);
    @Query("select b from Bill b JOIN b.paymentStatuses ps where DATEPART(MONTH, ps.paymentDate) =  Month(:date) and DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and b.status=5 or DATEPART(MONTH, ps.paymentDate) =  Month(:date) and DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and b.status=6")
    List<Bill> tongBillHuyMonth(Date date);
    @Query("select b from Bill b JOIN b.paymentStatuses ps where DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and b.status=5 or DATEPART(YEAR, ps.paymentDate) = YEAR(:date) and b.status=6")
    List<Bill> tongBillHuyYear(Date date);
    @Query("SELECT b FROM Bill b " +
            "JOIN b.paymentStatuses ps " +
            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
            "AND b.status = 5 OR ps.paymentDate BETWEEN :startDate AND :endDate and  b.status = 6 ")
    List<Bill> tongBillHuyOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //Tổng số đơn Trả
    @Query("select b from Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls where ps.paymentDate = :day AND bls.status = 32")
    List<Bill> tongBillTraHangDay(Date day);
    @Query("SELECT b FROM Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls WHERE DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :date) AND bls.status = 32")
    List<Bill> tongBillTraHangWeek(Date date);
    @Query("select b from Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls where DATEPART(MONTH, ps.paymentDate) =  Month(:date) and DATEPART(YEAR, ps.paymentDate) = YEAR(:date) AND bls.status = 32")
    List<Bill> tongBillTraHangMonth(Date date);
    @Query("select b from Bill b JOIN b.paymentStatuses ps JOIN b.billHistories bls where DATEPART(YEAR, ps.paymentDate) = YEAR(:date) AND bls.status = 32")
    List<Bill> tongBillTraHangYear(Date date);
    @Query("SELECT b FROM Bill b " +
            "JOIN b.paymentStatuses ps " +
            "JOIN b.billHistories bls " +
            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
            "AND bls.status = 32")

    List<Bill> tongBillTraHangOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //Tỏng số lượng bill theo trang thai
    @Query("select b from Bill b JOIN b.paymentStatuses ps where ps.paymentDate = :day and b.status = :status")
    List<Bill> tongStatusBillDay(@Param("day") Date day, @Param("status") Integer status);
    @Query("SELECT b FROM Bill b JOIN b.paymentStatuses ps " +
            "WHERE DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, :day) " +
            "AND DATEPART(WEEK, ps.paymentDate) = DATEPART(WEEK, :day) " +
            "AND b.status = :status " )
    List<Bill> tongStatusBillWeek(@Param("day") Date day, @Param("status") Integer status);
    @Query("SELECT b FROM Bill b JOIN b.paymentStatuses ps " +
            "WHERE FUNCTION('MONTH', ps.paymentDate) = FUNCTION('MONTH', :day) " +
            "AND FUNCTION('YEAR', ps.paymentDate) = FUNCTION('YEAR', :day) " +
            "AND b.status = :status " )
    List<Bill> tongStatusBillMonth(@Param("day") Date day, @Param("status") Integer status);
    @Query("SELECT b FROM Bill b JOIN b.paymentStatuses ps " +
            "WHERE FUNCTION('YEAR', ps.paymentDate) = FUNCTION('YEAR', :day) " +
            "AND b.status = :status ")
    List<Bill> tongStatusBillYear(@Param("day") Date day, @Param("status") Integer status);
    @Query("SELECT b FROM Bill b JOIN b.paymentStatuses ps " +
            "WHERE ps.paymentDate BETWEEN :startDate AND :endDate " +
            "AND b.status = :status "
            )
    List<Bill> tongStatusBillOption(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") Integer status);

    @Modifying
    @Transactional
    @Query("UPDATE Bill b SET b.status = 4 WHERE b.id = :id")
    void updateBillStatus(Long id);

    @Query("SELECT b FROM Bill b WHERE b.status = 1")
    Page<Bill> getAllBillChoThanhToan(Pageable pageable);
}
