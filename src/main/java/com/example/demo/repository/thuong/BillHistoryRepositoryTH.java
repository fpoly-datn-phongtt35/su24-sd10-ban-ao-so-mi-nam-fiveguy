package com.example.demo.repository.thuong;

import com.example.demo.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillHistoryRepositoryTH extends JpaRepository<BillHistory, Long> {

}
