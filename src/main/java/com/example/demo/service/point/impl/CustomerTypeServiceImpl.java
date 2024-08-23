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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class CustomerTypeServiceImpl implements CustomerTypeService {

    @Autowired
    private CustomerTypeRepository repository;



    private String generateCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000); // Sinh số ngẫu nhiên từ 100000 đến 999999
        return "LKH" + randomNumber;
    }


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
        // Kiểm tra xem mã code đã tồn tại hay chưa
        if (customerType.getCode() == null) {
            // Trường hợp lưu mới
//            if (repository.existsByCode(customerType.getCode())) {
//                throw new IllegalArgumentException("Code already exists");
//            }

            // Thiết lập mã code mới và các thuộc tính khác
            customerType.setCode(generateCode());
            customerType.setCreatedAt(new Date());
            customerType.setStatus(1);
            return repository.save(customerType);
        } else {
            // Trường hợp cập nhật
            if (!repository.existsByCode(customerType.getCode())) {
                throw new IllegalArgumentException("Code does not exist");
            }

            // Thực hiện cập nhật
            CustomerType existingCustomerType = repository.findByCode(customerType.getCode());
            if (existingCustomerType == null) {
                throw new IllegalArgumentException("CustomerType not found");
            }

            // Cập nhật thông tin
            existingCustomerType.setName(customerType.getName());
            existingCustomerType.setStatus(customerType.getStatus());
            // Cập nhật các trường khác nếu cần

            return repository.save(existingCustomerType);
        }
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