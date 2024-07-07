package com.example.demo.senderMail;//package com.example.demo.senderMail;
//
//import com.example.demo.service.AccountServiceH;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/email")
//@CrossOrigin(origins = {"*"})
//public class MailSenderController {
//    @Autowired
//    private UserServiceImpl emailSender;
//
//
//    @Autowired
//    private AccountServiceH accountService;
//
//    @GetMapping("/getAll")
//    public ResponseEntity<?> all() {
//        return   ResponseEntity.ok(accountService.getAll());
//    }
//
////    @PostMapping("")
////    public ResponseEntity<?> sendMail(@RequestBody MailSender email) {
////        emailSender.sendEmail(email.getToEmail(), email.getSubject(), email.getTitleEmail(), email.getBody());
////        return new ResponseEntity<>(HttpStatus.OK);
////    }
//}
