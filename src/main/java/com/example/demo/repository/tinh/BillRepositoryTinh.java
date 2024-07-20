package com.example.demo.repository.tinh;

import com.example.demo.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BillRepositoryTinh extends JpaRepository<Bill, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Bill b SET b.status = 4 WHERE b.id = :id")
    void updateBillStatus(Long id);

    @Query("SELECT b FROM Bill b WHERE b.status = 1")
    Page<Bill> getAllBillChoThanhToan(Pageable pageable);
}
