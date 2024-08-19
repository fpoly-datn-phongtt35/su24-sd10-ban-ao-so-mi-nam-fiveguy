package com.example.demo.service.Customer;



import com.example.demo.entity.Address;

import java.util.List;
import java.util.Optional;

public interface OlAddressService {

    List<Address> getAddressListByIdCustomer(Long id);

    void deleteAddress(Long id);

    boolean update(Address userInfoRequest);

    boolean addAddress(Address addressRequest);

    Address findByDefaultAddressTrue(Long id);

    Optional<Address> findById(Long id);
}
