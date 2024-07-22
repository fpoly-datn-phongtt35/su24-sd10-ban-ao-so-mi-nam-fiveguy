package com.example.demo.repository.nguyen.bill;

import com.example.demo.entity.ReturnOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NReturnOrderRepository extends JpaRepository<ReturnOrder, Long> {

    List<ReturnOrder> findAllReturnOrdersByBillId(Long billId);
}
