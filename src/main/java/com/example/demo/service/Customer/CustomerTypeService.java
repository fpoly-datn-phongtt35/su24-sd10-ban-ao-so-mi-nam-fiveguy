package com.example.demo.service.Customer;

import com.example.demo.entity.CustomerType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerTypeService {
    // get all
    List<CustomerType> getAll();

    Page<CustomerType> phanTrang(Integer pageNum, Integer pageNo);

    CustomerType create(CustomerType customerTypes);

    void delete(Long id);

    CustomerType update(Long id, CustomerType customerTypes);
}
