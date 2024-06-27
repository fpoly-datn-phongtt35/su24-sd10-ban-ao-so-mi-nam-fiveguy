package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.repository.SCAccountRepository;
import com.example.demo.security.repository.SCCustomerRepository;
import com.example.demo.security.service.SCCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class SCSCCustomerServiceImpl implements SCCustomerService {

    @Autowired
    private SCCustomerRepository SCCustomerRepository;

    @Autowired
    private SCAccountRepository SCAccountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Customer findByAccount_Id(Long accountId) {
        return SCCustomerRepository.findByAccount_Id(accountId);
    }

    @Override
    public Customer save(Customer customerEntity) {
        return SCCustomerRepository.save(customerEntity);
    }

    @Override
    public Customer createCustomer(Customer customerEntity) {
        Customer customer = new Customer();
        customer.setFullName(customerEntity.getFullName());
        customer.setAvatar(customerEntity.getAvatar());
        customer.setBirthDate(customerEntity.getBirthDate());
        customer.setGender(customerEntity.getGender());
        customer.setAccount(customerEntity.getAccount());
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        customer.setCreatedBy("Admin");
        customer.setUpdatedBy("Admin");
        customer.setStatus(1);
        return SCCustomerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomerByToken(String token) {
        try {
            // Validate token
            if (token == null || !token.startsWith("Bearer ")) {
                return Optional.empty();
            }

            // Extract actual token
            String actualToken = token.replace("Bearer ", "");

            // Get account name from token
            String accountName = jwtTokenUtil.getUsernameFromToken(actualToken);
            if (accountName == null) {
                return Optional.empty();
            }

            // Retrieve account
            Optional<Account> optionalAccount = SCAccountRepository.findByAccount(accountName);
            if (optionalAccount.isEmpty()) {
                return Optional.empty();
            }

            Account account = optionalAccount.get();

            // Check for Customer
            Customer customer = SCCustomerRepository.findByAccount_Id(account.getId());
            if (customer != null) {
                return Optional.of(customer);
            }
        } catch (Exception e) {
            // Log exception (use a logging framework in real applications)
            System.err.println("Error occurred while getting customer by token: " + e.getMessage());
        }

        return Optional.empty();
    }

}
