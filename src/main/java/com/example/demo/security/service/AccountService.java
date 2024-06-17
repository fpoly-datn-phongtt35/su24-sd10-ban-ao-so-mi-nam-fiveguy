package com.example.demo.security.service;

import com.example.demo.entity.Account;
import com.example.demo.security.Request.UserRequestDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {


    Optional<Account> findByAccount(String account);

    List<Account> findByEmail(String email);
    Account createAccount(Account accountEntity);
    List<UserRequestDTO> getAllAccount();

    Optional<Account> findByAccount2(String username);

    Optional<String> getFullNameByToken(String token);


}
