package com.example.demo.repository.nguyen;

import com.example.demo.entity.Voucher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface NVoucherRepository extends JpaRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {

    List<Voucher> findAllByOrderByCreatedAtDesc();

    @Query("SELECT v FROM Voucher v WHERE v.id = :id")
    Voucher findByIdN(@Param("id") Long id);

    boolean existsByCode(String code);

}
