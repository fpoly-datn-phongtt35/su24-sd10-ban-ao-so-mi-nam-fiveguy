package com.example.demo.repository.tinh;

import com.example.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepositoryTinh extends JpaRepository<Bill, Long> {
}
