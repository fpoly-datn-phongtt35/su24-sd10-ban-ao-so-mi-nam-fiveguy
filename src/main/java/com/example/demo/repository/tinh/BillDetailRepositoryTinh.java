package com.example.demo.repository.tinh;

import com.example.demo.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepositoryTinh extends JpaRepository<BillDetail, Long> {
}
