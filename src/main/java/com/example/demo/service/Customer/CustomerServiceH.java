package com.example.demo.service.Customer;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CustomerServiceH {
    // get all
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
    Customer getByAccount(String account);
    List<Customer> getAllStatus(Integer status);

    Page<Customer> findCustomer(String fullName, String code, String avatar, Date birthDate, Boolean gender, String address, String account, String email, String phoneNumber, Long id, Integer status, Pageable pageable);

}
