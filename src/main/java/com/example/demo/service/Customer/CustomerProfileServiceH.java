package com.example.demo.service.Customer;

import com.example.demo.entity.Account;
import com.example.demo.model.request.customer.UserInfoRequest;

import java.util.Optional;

public interface CustomerProfileServiceH {

    Optional<Account> findByAccount(String username);
//     List<UserRequestDTO> getAllAccount();
//    AccountEntity createAccount(AccountEntity accountEntity);

    boolean updateUser(UserInfoRequest userInfoRequest);

}
