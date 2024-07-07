package com.example.demo.service.Customer;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CustomerServiceH {
    // get all
    List<Customer> getAll();
    // et all customer status
    List<Customer> getAllStatusDangLam();

    Customer getById(Long id);

    Customer create(Customer customers);

    void delete(Long id);

    Customer update(Long id, Customer customers);

    Customer updateStatus(Long id, Customer customers);

    //     List<Customer> searchMa(@PathVariable String ma);
    Page<Customer>  searchMa(String ma, Integer page, Integer size);

    List<Customer> getAllStatus(Integer status);

    Page<Customer> findCustomer(String fullName, String code, String avatar, Date birthDate, Boolean gender, String account, String email, String phoneNumber, String name, Integer status, Pageable pageable);

}
