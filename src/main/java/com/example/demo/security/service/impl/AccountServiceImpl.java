package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.security.Request.UserRequestDTO;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.repository.AccountRepository;
import com.example.demo.security.repository.CustomerRepository;
import com.example.demo.security.repository.EmployeeRepository;
import com.example.demo.security.service.AccountService;
import com.example.demo.security.service.CustomerService;
import com.example.demo.security.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<Account> findByAccount(String username) {
        Optional<Account> account = accountRepository.findByAccount(username);
        if (account.isPresent()) {
            return account;
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findByEmail(String email) {
        List<Account> account = accountRepository.findByEmail(email);
        if (account != null){
            return account;
        }
        return null;
    }

    @Override
    public Account createAccount(Account accountEntity) {
        return accountRepository.save(accountEntity);
    }

    public  UserRequestDTO mapAccountToUserRequestDTO(Account account) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setAccount(account.getAccount());
        userRequestDTO.setEmail(account.getEmail());
        userRequestDTO.setId(account.getId());
        return userRequestDTO;
    }

    @Override
    public List<UserRequestDTO> getAllAccount() {
        List<Account> accounts = accountRepository.findAll();
        List<UserRequestDTO> userRequestDTOs = new ArrayList<>();
        for (Account account : accounts) {
            userRequestDTOs.add(mapAccountToUserRequestDTO(account));
        }
        return userRequestDTOs;
    }

    @Override
    public Optional<String> getFullNameByToken(String token) {
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
            Optional<Account> optionalAccount = accountRepository.findByAccount(accountName);
            if (optionalAccount.isEmpty()) {
                return Optional.empty();
            }

            Account account = optionalAccount.get();

            // Check for Employee
            Employee employee = employeeRepository.findByAccount_Id(account.getId());
            if (employee != null && employee.getFullName() != null) {
                return Optional.of(employee.getFullName());
            }

            // Check for Customer
            Customer customer = customerRepository.findByAccount_Id(account.getId());
            if (customer != null && customer.getFullName() != null) {
                return Optional.of(customer.getFullName());
            }

        } catch (Exception e) {
            // Log exception (use a logging framework in real applications)
            System.err.println("Error occurred while getting full name by token: " + e.getMessage());
        }

        return Optional.empty();
    }


    @Override
    public Optional<Account> findByAccount2(String accountName) {
        Optional<Account> account = accountRepository.findByAccount(accountName);
        if (account.isPresent()){
            return account;
        }
        return Optional.empty();
    }


}
