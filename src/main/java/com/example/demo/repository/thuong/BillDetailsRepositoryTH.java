package com.example.demo.repository.thuong;

import com.example.demo.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillDetailsRepositoryTH extends JpaRepository<BillDetail, Long> {
    BillDetail findByProductDetail_Id(Long id);
    List<BillDetail> findAllByBill_Id(Long id);
}
