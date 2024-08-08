package com.example.demo.model.request.customer;


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

public class UserInfoRequest {
    private Long id;

    private String fullName;
    private com.example.demo.model.request.customer.AccountInfoRequest account;
//    private String birthday;
    private Boolean gender;
    private String avatar;


}
