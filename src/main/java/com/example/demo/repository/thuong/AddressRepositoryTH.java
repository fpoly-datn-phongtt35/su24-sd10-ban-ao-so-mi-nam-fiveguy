package com.example.demo.repository.thuong;

import com.example.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepositoryTH extends JpaRepository<Address, Long> {
    List<Address> findAllByCustomer_Id(Long id);

    Optional<Address> findByCustomerIdAndDefaultAddressTrue(Long customerId);

    List<Address> findByCustomer_Id(Long Id);
}
