package com.example.demo.restController.Customer;

import com.example.demo.model.request.customer.AccountChangePass;
import com.example.demo.model.request.customer.UserInfoRequest;
import com.example.demo.security.service.SCUserService;
import com.example.demo.service.Customer.CustomerProfileServiceH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ol/authenticated")

public class CustomerProfileRestControllerH {
    @Autowired
    private CustomerProfileServiceH olAccountService;

    @Autowired
    private SCUserService userService;


    @PostMapping("/updateUser")
    public boolean updateUser(@RequestBody UserInfoRequest userInfoRequest) {
        return olAccountService.updateUser(userInfoRequest);
    }

    @PostMapping("/resetPassword")
    public boolean resetPassword(@RequestBody AccountChangePass userInfoRequest) {
        return userService.resetPassword(userInfoRequest.getUsername(), userInfoRequest.getNewPassword());
    }
}
