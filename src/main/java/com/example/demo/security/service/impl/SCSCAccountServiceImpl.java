package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.security.Request.UserRequestDTO;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.repository.SCAccountRepository;
import com.example.demo.security.repository.SCCustomerRepository;
import com.example.demo.security.repository.SCEmployeeRepository;
import com.example.demo.security.service.SCAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SCSCAccountServiceImpl implements SCAccountService {


    @Autowired
    private SCAccountRepository SCAccountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SCEmployeeRepository SCEmployeeRepository;

    @Autowired
    private SCCustomerRepository SCCustomerRepository;

    @Override
    public Optional<Account> findByAccount(String username) {
        Optional<Account> account = SCAccountRepository.findByAccount(username);
        if (account.isPresent()) {
            return account;
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findByEmail(String email) {
        List<Account> account = SCAccountRepository.findByEmail(email);
        if (account != null){
            return account;
        }
        return null;
    }

    @Override
    public Account createAccount(Account accountEntity) {
        return SCAccountRepository.save(accountEntity);
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
        List<Account> accounts = SCAccountRepository.findAll();
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
            Optional<Account> optionalAccount = SCAccountRepository.findByAccount(accountName);
            if (optionalAccount.isEmpty()) {
                return Optional.empty();
            }

            Account account = optionalAccount.get();

            // Check for Employee
            Employee employee = SCEmployeeRepository.findByAccount_Id(account.getId());
            if (employee != null && employee.getFullName() != null) {
                return Optional.of(employee.getFullName());
            }

            // Check for Customer
            Customer customer = SCCustomerRepository.findByAccount_Id(account.getId());
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
        Optional<Account> account = SCAccountRepository.findByAccount(accountName);
        if (account.isPresent()){
            return account;
        }
        return Optional.empty();
    }


}
