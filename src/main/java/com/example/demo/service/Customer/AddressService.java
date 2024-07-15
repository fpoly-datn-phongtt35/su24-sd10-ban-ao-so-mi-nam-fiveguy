package com.example.demo.service.Customer;

import com.example.demo.entity.Address;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    List<Address> getAllAddress();

    List<Address> findByCustomerId(Long customerId);

//    Page<Address> getAll(Integer page);

    Address getAddressById(Long id);

    Page<Address> getAllAddressPage(Integer page);

    Address createAddress(Address addressEntity);

    Address updateAddress(Address addressEntity, Long id);

    void deleteAddress(Long id);

    List<Address> getSStatus(Integer status);

// //OL
    List<Address> getAddressListByUsername(String username);

//    void deleteAddress(Long id);

    boolean update(Address userInfoRequest);

    boolean addAddress(Address addressRequest);

    Address findByDefaultAddressTrue(String username);

    Optional<Address> findById(Long id);
// // END OL
}
