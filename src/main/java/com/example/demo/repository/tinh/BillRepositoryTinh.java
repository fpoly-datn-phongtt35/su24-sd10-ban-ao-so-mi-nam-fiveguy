package com.example.demo.repository.tinh;

import com.example.demo.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface BillRepositoryTinh extends JpaRepository<Bill, Long> {
    //Tống số tiền
    @Query("SELECT COALESCE(SUM(v.totalAmountAfterDiscount), 0) AS totalAmount FROM Bill v where v.paymentDate = :date and v.status = 4")
    BigDecimal tongSoTienDay(Date date);
    @Query("SELECT COALESCE(SUM(v.totalAmountAfterDiscount), 0) AS totalAmount " +
            "FROM Bill v " +
            "WHERE DATEPART(YEAR, v.paymentDate) = YEAR(:date) AND v.status = 4 " +
            "AND DATEPART(WEEK, v.paymentDate) = DATEPART(WEEK, :date)")
    BigDecimal tongSoTienWeek(Date date);

    @Query("SELECT COALESCE(SUM(v.totalAmountAfterDiscount), 0) AS totalAmount FROM Bill v where DATEPART(MONTH, v.paymentDate) =  Month(:date) and DATEPART(YEAR, v.paymentDate) = YEAR(:date) and v.status=4")
    BigDecimal tongSoTienMonth(Date date);
    @Query("SELECT COALESCE(SUM(v.totalAmountAfterDiscount), 0) AS totalAmount FROM Bill v where DATEPART(YEAR, v.paymentDate) = YEAR(:date) and v.status=4")
    BigDecimal tongSoTienYear(Date date);

    //Tổng số dơn thanhf cong
    @Query("select b from Bill b where b.paymentDate = :day and b.status=4")
    List<Bill> tongBillThanhCongDay(Date day);
    @Query("SELECT b FROM Bill b WHERE DATEPART(YEAR, b.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.paymentDate) = DATEPART(WEEK, :date) AND b.status = 4")
    List<Bill> tongBillThanhCongWeek(Date date);
    @Query("select b from Bill b where DATEPART(MONTH, b.paymentDate) =  Month(:date) and DATEPART(YEAR, b.paymentDate) = YEAR(:date) and b.status=4")
    List<Bill> tongBillThanhCongMonth(Date date);
    @Query("select b from Bill b where DATEPART(YEAR, b.paymentDate) = YEAR(:date) and b.status=4")
    List<Bill> tongBillThanhCongYear(Date date);

    //Tổng số dơn thanhf cong
    @Query("select b from Bill b where b.paymentDate = :day and b.status= 5 or b.paymentDate = :day and b.status= 6")
    List<Bill> tongBillHuyDay(Date day);
    @Query("SELECT b FROM Bill b WHERE DATEPART(YEAR, b.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.paymentDate) = DATEPART(WEEK, :date) AND b.status=5 or DATEPART(YEAR, b.paymentDate) = DATEPART(YEAR, :date) AND DATEPART(WEEK, b.paymentDate) = DATEPART(WEEK, :date) AND b.status=6")
    List<Bill> tongBillHuyWeek(Date date);
    @Query("select b from Bill b where DATEPART(MONTH, b.paymentDate) =  Month(:date) and DATEPART(YEAR, b.paymentDate) = YEAR(:date) and b.status=5 or DATEPART(MONTH, b.paymentDate) =  Month(:date) and DATEPART(YEAR, b.paymentDate) = YEAR(:date) and b.status=6")
    List<Bill> tongBillHuyMonth(Date date);
    @Query("select b from Bill b where DATEPART(YEAR, b.paymentDate) = YEAR(:date) and b.status=5 or DATEPART(YEAR, b.paymentDate) = YEAR(:date) and b.status=6")
    List<Bill> tongBillHuyYear(Date date);

    @Modifying
    @Transactional
    @Query("UPDATE Bill b SET b.status = 4 WHERE b.id = :id")
    void updateBillStatus(Long id);

    @Query("SELECT b FROM Bill b WHERE b.status = 1")
    Page<Bill> getAllBillChoThanhToan(Pageable pageable);
}
