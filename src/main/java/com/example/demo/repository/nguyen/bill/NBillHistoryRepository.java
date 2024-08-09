package com.example.demo.repository.nguyen.bill;

import com.example.demo.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NBillHistoryRepository extends JpaRepository<BillHistory, Long> {

    BillHistory findBillHistoryByBillId(Long id);

    List<BillHistory> findByBillIdOrderByCreatedAtAsc(Long billId);

    BillHistory findAllByBillIdAndStatus(Long billId, Integer status);
}
