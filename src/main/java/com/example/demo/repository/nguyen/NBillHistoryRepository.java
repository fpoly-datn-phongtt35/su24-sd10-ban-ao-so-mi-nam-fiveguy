package com.example.demo.repository.nguyen;

import com.example.demo.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NBillHistoryRepository extends JpaRepository<BillHistory, Long> {

    BillHistory findBillHistoryByBillId(Long id);
}
