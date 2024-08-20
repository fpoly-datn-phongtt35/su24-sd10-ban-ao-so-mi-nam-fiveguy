package com.example.demo.repository.thuong;

import com.example.demo.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillDetailsRepositoryTH extends JpaRepository<BillDetail, Long> {
    Optional<BillDetail> findByBill_IdAndProductDetail_Id(Long billId, Long productDetailId);
    List<BillDetail> findAllByBill_Id(Long id);
}
