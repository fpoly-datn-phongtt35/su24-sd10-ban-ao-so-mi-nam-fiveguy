package com.example.demo.repository.Customer;

import com.example.demo.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepositoryH extends JpaRepository<Bill, Long> {

//    List<Bill> findAllByOrderByCreatedAtDesc();
////OL
////    @Query("SELECT b FROM Bill b WHERE b.customer.id = :customerId ORDER BY b.createdAt DESC")
////    Page<Bill> findLatestBillByCustomerId(Long customerId, Pageable pageable);
//
//
//    Optional<Bill> findById(Long id);
//
//    List<Bill> findByPhoneNumberOrderByCreatedAtDesc(String pn);

//END OL
}
