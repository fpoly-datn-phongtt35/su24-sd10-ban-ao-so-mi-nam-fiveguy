package com.example.demo.security.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CheckRequest {
    private String email;
    private String account;
    private String phoneNumber;

}
