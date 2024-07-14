package com.example.demo.service.onlineShop;

import com.example.demo.entity.Address;
import com.example.demo.entity.Customer;

import java.util.List;

public interface OLAddressService2 {

    List<Address> getAddressListByUsername(Customer customer);

    Address findByDefaultAddressTrue(Customer customer);


}
