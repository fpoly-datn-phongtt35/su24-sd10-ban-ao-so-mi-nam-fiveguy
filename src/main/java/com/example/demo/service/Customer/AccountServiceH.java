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

    Account saveAccountEmployee(Account accountEntity);


    Account updateAccount(Account accountEntity, Long id);

    void deleteAccount(Long id);

    Account save(Account accountEntity);

    List<Account> getAll();

    List<Account> getSStatus(Integer status);

// //   Tôi viết thêm hàm này nhiệm vụ security

    Optional<Account> findByAccount2(String username);

//    List<UserRequestDTO> getAllAccount2();

    Account createAccount2(Account accountEntity);

    List<Account> findByEmail(String email);

// //OL
    Optional<Account> findByAccountLogin(String username);

    Account saveAccountCustomer(Account accountEntity);
////     List<UserRequestDTO> getAllAccount();
////    AccountEntity createAccount(AccountEntity accountEntity);
//
//    boolean updateUser(UserInfoRequest userInfoRequest);
// // END OL

//    Nhánh Tịnh
    Account getByEmailAccount(String id);

    Account updateAccountEmail(Account accountEntity, String email);

    Account updateAccountEmailDetailEmployee(Account accountEntity, String email);

    boolean checkEmailExists(String email);

    boolean checkAccountExists(String account);

    boolean checkPhoneNumberExists(String account);
}
