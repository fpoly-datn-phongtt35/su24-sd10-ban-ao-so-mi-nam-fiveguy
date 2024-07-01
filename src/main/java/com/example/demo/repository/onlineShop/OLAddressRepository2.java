package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface OLAddressRepository2 extends JpaRepository<Address, Long> {

    List<Address> findAllByCustomer_IdAndStatus(Long Id, int status);

    List<Address> findByCustomer_Id(Long Id);
}
