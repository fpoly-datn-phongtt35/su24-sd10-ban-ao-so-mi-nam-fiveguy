package com.example.demo.service.Customer.ServiceImpl;


import com.example.demo.entity.Address;
import com.example.demo.repository.Customer.OLAddressRepository;
import com.example.demo.service.Customer.OlAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class OlAddressServiceImpl implements OlAddressService {

    @Autowired
    private OLAddressRepository repository;



    @Override
    public List<Address> getAddressListByIdCustomer(Long id) {

        // Lấy thông tin khách hàng từ tài khoản
        return repository.findAllByCustomer_IdAndStatus(id,1);
    }

    @Override
    public void deleteAddress(Long id) {
        repository.deleteById(id);

    }

    @Override
    public boolean update(Address addressRequest) {
        if (addressRequest.getDefaultAddress() && addressRequest.getDefaultAddress() != null) {
            // Lấy danh sách địa chỉ của khách hàng
            List<Address> customerAddresses = repository.findAllByCustomer_IdAndStatus(addressRequest.getCustomer().getId(),1);
            for (Address address : customerAddresses) {
                if (!address.getId().equals(addressRequest.getId())) {
                    address.setDefaultAddress(false);
                    repository.save(address);
                }
            }
        }

        addressRequest.setUpdatedAt(new Date());
        repository.save(addressRequest);
        return true;
    }

    @Override
    public boolean addAddress(Address addressRequest) {
        if (addressRequest.getDefaultAddress() && addressRequest.getDefaultAddress() != null) {
            // Lấy danh sách địa chỉ của khách hàng
            List<Address> customerAddresses = repository.findAllByCustomer_IdAndStatus(addressRequest.getCustomer().getId(),1);
            for (Address address : customerAddresses) {
                if (!address.getId().equals(addressRequest.getId())) {
                    address.setDefaultAddress(false);
                    repository.save(address);
                }
            }
        }
        addressRequest.setCreatedAt(new Date());
        addressRequest.setStatus(1);
        addressRequest.setId(null);
        repository.save(addressRequest);
        return true;
    }

    @Override
    public Address findByDefaultAddressTrue(Long id) {

        List<Address> addressEntities = repository.findAllByCustomer_IdAndStatus(id,1);
        for (Address addressEntity : addressEntities) {
            if ((addressEntity.getDefaultAddress())) {
                return addressEntity;
            }
        }
        return null;
    }


    @Override
    public Optional<Address> findById(Long id) {
        Optional<Address> addressEntity = repository.findById(id);
        if (addressEntity.isPresent()){
            return addressEntity;
        }

        return Optional.empty();
    }


}
