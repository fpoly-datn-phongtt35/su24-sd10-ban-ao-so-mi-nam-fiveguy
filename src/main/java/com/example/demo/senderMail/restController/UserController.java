package com.example.demo.senderMail.restController;//package com.example.demo.senderMail.restController;
//
//
//import com.example.demo.entity.AccountEntity;
//import com.example.demo.senderMail.Respone.ResponseObject;
//import com.example.demo.security.service.UserServiceImpl;
//import com.example.demo.service.serviceiplm.AccountServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    private AccountServiceImpl accountService;
//
//    public UserController(UserServiceImpl userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/register")
//    public ResponseObject register(@RequestBody AccountEntity user) {
//        return userService.register(user);
//    }
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<AccountEntity> updateIsActive(@PathVariable Long id) {
//        AccountEntity existingAccount = accountService.getAccountById(id);
//        if (existingAccount != null) {
//            existingAccount.setActive(true); // Cập nhật trường isActive thành true
//            AccountEntity updatedAccount = accountService.updateAccount(existingAccount, id);
//            return ResponseEntity.ok(updatedAccount);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PostMapping("/active")
//    public ResponseObject active(@RequestBody AccountEntity user) {
//        return userService.active(user);
//    }
//
//    @PostMapping("/forgot-password/{email}")
//    public ResponseObject forgotPassword(@PathVariable String email) {
//        return userService.forgotPassword(email);
//    }
////    http://localhost:8080/user/re-send/usernam2e22222
//
//    @PostMapping("/re-send/{account}")
//    public ResponseObject reSend(@PathVariable String account) {
//        return userService.reSendOTP(account);
//    }
//
//}
