package com.example.demo.repository.point;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerPointsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerPointsHistoryRepository extends JpaRepository<CustomerPointsHistory, Long> {
    List<CustomerPointsHistory> findByCustomerId(Long customerId);
    List<CustomerPointsHistory> findByCustomer(Customer customer);
    Optional<CustomerPointsHistory> findByBillId(Long billId);
}