package com.example.demo.repository.Customer;

import com.example.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLAddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByCustomer_IdAndStatus(Long Id,int status);

    List<Address> findByCustomer_FullName(String userName);

}
