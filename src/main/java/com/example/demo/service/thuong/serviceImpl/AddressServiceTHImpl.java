package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Address;
import com.example.demo.repository.thuong.AddressRepositoryTH;
import com.example.demo.service.thuong.AddressServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceTHImpl implements AddressServiceTH {
    @Autowired
    private AddressRepositoryTH repository;
    @Override
    public List<Address> saveAll(List<Address> list) {
        return repository.saveAll(list);
    }

    @Override
    public Address update(Address addressRequest) {
        Optional<Address> addressOptional = repository.findById(addressRequest.getId());
        if (addressOptional.isPresent()) {
            if (addressOptional.get().getDefaultAddress() == false && addressRequest.getDefaultAddress() == true) {
               List<Address> addressList = repository.findAllByCustomer_Id(addressOptional.get().getCustomer().getId());
               addressList.forEach(c -> {
                   if (c.getDefaultAddress() == true) {
                       c.setDefaultAddress(false);
                   }
               });
               repository.saveAll(addressList);
            }
            Address address = addressOptional.get();
            address.setAddressId(addressRequest.getAddressId());
            address.setAddress(addressRequest.getAddress());
            address.setPhoneNumber(addressRequest.getPhoneNumber());
            address.setName(addressRequest.getName());
            address.setUpdatedAt(new Date());
            address.setDefaultAddress(addressRequest.getDefaultAddress());
            repository.save(address);
        }
        return null;
    }

    @Override
    public Address deleteAddress(Long id) {
        Address address = repository.findById(id).orElse(null);
        repository.delete(address);
        return address;
    }

    @Override
    public Address getDefaultAddressByCustomerId(Long customerId) {
        Optional<Address> address =repository.findByCustomerIdAndDefaultAddressTrue(customerId);
        if (address.isPresent()){
            return address.get();
        }
        return null;
    }
}
