package com.example.demo.repository.nguyen;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NCustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE " +
            "(:fullName IS NULL OR c.fullName LIKE %:fullName%) AND " +
            "(:phoneNumber IS NULL OR c.account.phoneNumber LIKE %:phoneNumber%) AND " +
            "(:email IS NULL OR c.account.email LIKE %:email%) AND " +
            "(:customerTypeId IS NULL OR c.customerType.id = :customerTypeId)")
    Page<Customer> findCustomers(@Param("fullName") String fullName,
                                 @Param("phoneNumber") String phoneNumber,
                                 @Param("email") String email,
                                 @Param("customerTypeId") Long customerTypeId,
                                 Pageable pageable);
}
