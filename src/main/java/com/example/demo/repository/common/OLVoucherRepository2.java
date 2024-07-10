package com.example.demo.repository.common;

import com.example.demo.entity.Size;
import com.example.demo.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLVoucherRepository2 extends JpaRepository<Voucher, Long> {

    @Query("SELECT v FROM Voucher v WHERE v.status IN (1, 3) AND v.applyfor = 0 ORDER BY v.minimumTotalAmount DESC")
    List<Voucher> findAllByStatusAndApplyFor();

    @Query("SELECT v FROM Voucher v WHERE v.status = 1 AND v.applyfor = 0")
    List<Voucher> findAllByStatus1AndApplyFor();

    @Query("SELECT v FROM Voucher v WHERE v.id IN :ids AND v.status = 1")
    List<Voucher> findAllByIdAndStatus(@Param("ids") List<Long> ids);
}
