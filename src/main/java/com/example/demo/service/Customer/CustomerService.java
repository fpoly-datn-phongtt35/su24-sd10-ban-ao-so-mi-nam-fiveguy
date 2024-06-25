package com.example.demo.service.Customer;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    // get all
    List<Customer> getAll();

    // et all employee status
    List<Customer> getAllStatusDangLam();

    Customer getById(Long id);

    Page<Customer> phanTrang(Integer pageNum, Integer pageNo);

    Customer create(Customer customers);

    void delete(Long id);

    Customer update(Long id, Customer customers);

    Customer updateRole(Long id, Customer customers);

    //     List<Employees> searchMa(@PathVariable String ma);
    Page<Customer> searchMa(String ma, Integer page, Integer size);

    List<Customer> getAllStatus(Integer status);
}
