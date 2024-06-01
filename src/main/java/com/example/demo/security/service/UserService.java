package com.example.demo.security.service;


import com.example.demo.security.Request.TokenResponse;
import com.example.demo.security.Request.UserRequestDTO;
import com.example.demo.security.jwt_model.JwtRequest;
import com.example.demo.senderMail.Respone.ResponseObject;

public interface UserService {

    ResponseObject register(UserRequestDTO userRequestDTO);

    TokenResponse login(JwtRequest authenticationRequest) throws Exception;

    ResponseObject reSendOTP(String mail);

    ResponseObject forgotPassword(String email);

    Boolean confirmOTP(String email, String otp);

    void sendSimpleEmail(String toEmail, String text, String subject);

    boolean resetPassword(String email, String newPassword);

}
