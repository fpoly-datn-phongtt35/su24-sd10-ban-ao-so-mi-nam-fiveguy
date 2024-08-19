package com.example.demo.repository.onlineShop;

import com.example.demo.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLBillHistoryRepository2 extends JpaRepository<BillHistory, Long> {

    List<BillHistory> findByBillId(Long billId);
}
