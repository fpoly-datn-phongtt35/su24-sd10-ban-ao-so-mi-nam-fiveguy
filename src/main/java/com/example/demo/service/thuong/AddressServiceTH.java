package com.example.demo.service.thuong;

import com.example.demo.entity.Address;

import java.util.List;

public interface AddressServiceTH {
    List<Address> saveAll(List<Address> list);
    Address update(Address address);
    Address deleteAddress (Long id);
}
