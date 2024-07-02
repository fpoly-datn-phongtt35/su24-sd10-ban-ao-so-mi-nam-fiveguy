package com.example.demo.security.Request;

public class OTPConfirmationDTO {
    private String email;
    private String enteredOTP;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnteredOTP() {
        return enteredOTP;
    }

    public void setEnteredOTP(String enteredOTP) {
        this.enteredOTP = enteredOTP;
    }
}
