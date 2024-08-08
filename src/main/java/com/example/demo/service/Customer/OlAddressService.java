package com.example.demo.service.Customer;



import com.example.demo.entity.Address;

import java.util.List;
import java.util.Optional;

public interface OlAddressService {

   List<Address> getAddressListByUsername(String username);

   void deleteAddress(Long id);

    boolean update(Address userInfoRequest);

    boolean addAddress(Address addressRequest);

    Address findByDefaultAddressTrue(String username);

    Optional<Address> findById(Long id);
}
