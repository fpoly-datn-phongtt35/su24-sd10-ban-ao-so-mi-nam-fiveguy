package com.example.demo.senderMail.util;//package com.example.demo.senderMail.util;
//
//import com.example.demo.entity.AccountEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class Helper {
//    public AccountEntity getUser(String account, List<AccountEntity> list){
//        return list.stream().filter(user -> user.getAccount().equals(account)).findAny().orElse(null);
//    }
//
//    public String generateOTP(){
//        Random random = new Random();
//        String otp =String.valueOf(100000 + random.nextInt(9000000));
//        return otp;
//    }
//
//}
