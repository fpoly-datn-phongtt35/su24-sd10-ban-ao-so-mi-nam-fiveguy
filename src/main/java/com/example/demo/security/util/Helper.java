package com.example.demo.security.util;

import com.example.demo.security.Request.UserRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class Helper {
    public UserRequestDTO getUser(String account, List<UserRequestDTO> list) {
        return list.stream()
                .filter(user -> account != null && account.equals(user.getAccount()))
                .findAny()
                .orElse(null);
    }

    public UserRequestDTO getUserByEmail(String email, List<UserRequestDTO> list) {
        return list.stream()
                .filter(user -> email != null && email.equals(user.getEmail()))
                .findAny()
                .orElse(null);
    }

    public String generateOTP(){
        Random random = new Random();
        String otp =String.valueOf(100000 + random.nextInt(9000000));
        return otp;
    }

}
