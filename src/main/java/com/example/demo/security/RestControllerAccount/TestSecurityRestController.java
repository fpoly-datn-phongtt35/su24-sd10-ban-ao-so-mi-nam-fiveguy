package com.example.demo.security.RestControllerAccount;


import com.example.demo.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class TestSecurityRestController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = "/test", produces = "text/plain")
    public String hi(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String name = jwtTokenUtil.getUsernameFromToken(actualToken);
        return "Hello, " + " Token " + actualToken + " Name " + name;
    }
}