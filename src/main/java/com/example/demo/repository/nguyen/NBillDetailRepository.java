package com.example.demo.repository.nguyen;

import com.example.demo.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NBillDetailRepository extends JpaRepository<BillDetail, Long> {

    List<BillDetail> findAllByBillIdOrderByIdDesc(Long billId);
}
