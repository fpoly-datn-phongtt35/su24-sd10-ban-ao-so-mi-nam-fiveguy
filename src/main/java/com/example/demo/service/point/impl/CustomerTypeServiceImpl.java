package com.example.demo.service.point.impl;


import com.example.demo.entity.CustomerType;
import com.example.demo.repository.point.CustomerTypeRepository;
import com.example.demo.service.point.CustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerTypeServiceImpl implements CustomerTypeService {

    @Autowired
    private CustomerTypeRepository repository;

    @Override
    public Page<CustomerType> getCustomerTypes(String name, Pageable pageable) {
        Specification<CustomerType> spec = CustomerTypeSpecification.byName(name);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return repository.findAll(spec, sortedPageable);
    }

    @Override
    public CustomerType getCustomerTypeById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public CustomerType saveCustomerType(CustomerType customerType) {
        if (repository.existsByCode(customerType.getCode())) {
            throw new IllegalArgumentException("Code already exists");
        }
        customerType.setCreatedAt(new Date());
        customerType.setStatus(1);
        return repository.save(customerType);
    }

    @Override
    public CustomerType updateCustomerType(CustomerType customerType) {
        if (customerType.getId() == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        customerType.setUpdatedAt(new Date());
        return repository.save(customerType);
    }
}