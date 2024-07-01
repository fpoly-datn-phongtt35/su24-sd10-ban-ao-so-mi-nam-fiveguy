package com.example.demo.senderMail.Respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ConfirmationRequest {
    private String email;
    private String otp;
}
