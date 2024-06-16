package com.example.demo.security.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.RefreshToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken roles);

    void delete(Long id);

    RefreshToken createRefreshToken(UserDetails userDetails, Account accountEntity);

    void deleteByAccount(Account accountEntity);

    Account verifyExpiration(String token);
}
