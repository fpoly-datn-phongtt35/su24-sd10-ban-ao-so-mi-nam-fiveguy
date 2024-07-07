package com.example.demo.service.Customer;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerTypeServiceH {
    // get all
    List<CustomerType> getAll();

    CustomerType getById(Long id);

    Page<CustomerType> phanTrang(Integer pageNum, Integer pageNo);

    CustomerType create(CustomerType customerTypes);

    void delete(Long id);

    CustomerType update(Long id, CustomerType customerTypes);
}
