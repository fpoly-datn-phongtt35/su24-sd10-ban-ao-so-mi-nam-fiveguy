package com.example.demo.repository.tinh;

import com.example.demo.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BillHistoryRepositoryTinh extends JpaRepository<BillHistory, Long>, JpaSpecificationExecutor<BillHistory> {
}