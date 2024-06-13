package com.example.demo.service.serviceImpl.nguyen;

import com.example.demo.entity.CustomerType;
import com.example.demo.repository.nguyen.NCustomerTypeRepository;
import com.example.demo.service.nguyen.NCustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NCustomerTypeSerivceImpl implements NCustomerTypeService {

    @Autowired
    NCustomerTypeRepository customerTypeRepository;

    @Override
    public List<CustomerType> getAllCustomerType() {
        return customerTypeRepository.findAll();
    }

    @Override
    public CustomerType getCustomerTypeById(Long id) {
        return customerTypeRepository.findById(id).get();
    }
}
