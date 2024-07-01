package com.example.demo.restController.Customer;

import com.example.demo.entity.Account;
import com.example.demo.entity.Employee;
import com.example.demo.service.Customer.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/account")
@CrossOrigin("*")

public class AccountRestControllerH {
    @Autowired
    AccountService accountService;

    @GetMapping("")
    public ResponseEntity<List<Account>> getAllAccount() {
        List<Account> account = accountService.getAllAccount();
        return ResponseEntity.ok(account);
    }


    @GetMapping("/timkiem-status/{st}")
    public ResponseEntity<?> getSStatus(@PathVariable Integer st){
        return ResponseEntity.ok(accountService.getSStatus(st));
    }
    @GetMapping("/not-in-customer-employee")
    public List<Account> loadAccount() {
        return accountService.loadAccount();
    }



//    @PostMapping("/check-email")
//    public ResponseEntity<Object> checkEmailExists(@RequestBody CheckRequest checkRequest) {
//        boolean exists = accountRepository.existsByEmail(checkRequest.getEmail());
//        return ResponseEntity.ok().body(exists);
//    }

//    @PostMapping("/check-account")
//    public ResponseEntity<Object> checkAccountExists(@RequestBody CheckRequest checkRequest) {
//        boolean exists = accountRepository.existsByAccount(checkRequest.getAccount());
//        return ResponseEntity.ok().body(exists);
//    }
//
//    @PostMapping("/check-phone-number")
//    public ResponseEntity<Object> checkPhoneNumberExists(@RequestBody CheckRequest checkRequest) {
//        boolean exists = accountRepository.existsByPhoneNumber(checkRequest.getPhoneNumber());
//        return ResponseEntity.ok().body(exists);
//    }

    @PostMapping("")
    public ResponseEntity<Account> createAccount(@RequestBody Account accountEntity) {
        Account createdAccount = accountService.createAccount(accountEntity);

        if (createdAccount != null) {
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@RequestBody Account accountEntity, @PathVariable Long id) {
        Account account = accountService.updateAccount(accountEntity, id);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

//     Nhánh Tịnh -------------------------------------------------

    @GetMapping("/{email}")
    public ResponseEntity<Account> getByEmailAccount(@PathVariable String email) {
        Account account = accountService.getByEmailAccount(email);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/saveAccountEmployee")
    public ResponseEntity<?> saveAccountEmployee(@RequestBody Account accountEntity) {
        try {
            Account save = accountService.saveAccountEmployee(accountEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(save);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<Account> updateEmailAccount(@RequestBody Account accountEntity, @PathVariable String email) {
        Account account = accountService.updateAccountEmail(accountEntity, email);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean emailExists = accountService.checkEmailExists(email);
        return ResponseEntity.ok(emailExists);
    }

    @GetMapping("/check-account")
    public ResponseEntity<Boolean> checkAccount(@RequestParam String account) {
        boolean accountExists = accountService.checkAccountExists(account);
        return ResponseEntity.ok(accountExists);
    }

    @GetMapping("/check-phone-number")
    public ResponseEntity<Boolean> checkPhoneNumber(@RequestParam String phoneNumber) {
        boolean phoneNumberExists = accountService.checkPhoneNumberExists(phoneNumber);
        return ResponseEntity.ok(phoneNumberExists);
    }
}
