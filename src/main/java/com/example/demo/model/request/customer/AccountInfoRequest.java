package com.example.demo.model.request.customer;

import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AccountInfoRequest {
    private Long id;
//    private String account;
    private String email;
    private String phoneNumber;
    private Role role;
}
