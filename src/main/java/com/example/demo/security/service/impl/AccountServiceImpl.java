package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.security.Request.UserRequestDTO;
import com.example.demo.security.repository.AccountRepository;
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
    public Optional<Account> findByAccount2(String username) {
        Optional<Account> account = accountRepository.findByAccount(username);
        if (account.isPresent()){
            return account;
        }
        return Optional.empty();
    }


}
