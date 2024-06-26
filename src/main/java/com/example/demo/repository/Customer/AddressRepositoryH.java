package com.example.demo.repository.Customer;

import com.example.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepositoryH extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId")
    List<Address> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Address a WHERE a.status = :a")
    List<Address> getSStatus(Integer a);
//OL
    List<Address> findAllByCustomer_IdAndStatus(Long Id,int status);

    List<Address> findByCustomer_FullName(String userName);
//END OL
}
