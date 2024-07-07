package com.example.demo.service.Customer;

import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AccountServiceH {

    List<Account> getAllAccount();

    List<Account> loadAccount();

    Account getAccountById(Long id);

    Optional<Account> findByAccount(String account);

    Page<Account> getAllAccountPage(Integer page);

    Account createAccount(Account accountEntity);

    Account save(Account accountEntity);

    Account updateAccount(Account accountEntity, Long id);

    void deleteAccount(Long id);

    List<Account> getAll();

    List<Account> getSStatus(Integer status);

// //   Tôi viết thêm hàm này nhiệm vụ security

    Optional<Account> findByAccount2(String username);

//    List<UserRequestDTO> getAllAccount2();

    Account createAccount2(Account accountEntity);

    List<Account> findByEmail(String email);

// //OL
    Optional<Account> findByAccountLogin(String username);
////     List<UserRequestDTO> getAllAccount();
////    AccountEntity createAccount(AccountEntity accountEntity);
//
//    boolean updateUser(UserInfoRequest userInfoRequest);
// // END OL
}
