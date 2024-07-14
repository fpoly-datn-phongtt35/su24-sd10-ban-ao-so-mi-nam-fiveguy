package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Address;
import com.example.demo.entity.Customer;
import com.example.demo.repository.onlineShop.OLAddressRepository2;
import com.example.demo.service.onlineShop.OLAddressService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OLAddressServiceImpl2 implements OLAddressService2 {

    @Autowired
    private OLAddressRepository2 repository;

    @Override
    public List<Address> getAddressListByUsername(Customer customer) {

                return repository.findAllByCustomer_IdAndStatus(customer.getId(),1);
    }

    @Override
    public Address findByDefaultAddressTrue(Customer customer) {

                List<Address> addressEntities = repository.findByCustomer_Id(customer.getId());
                for (Address addressEntity : addressEntities) {
                    if ((addressEntity.getDefaultAddress())) {
                        return addressEntity;
                    }
                }
        return null;
    }



}
