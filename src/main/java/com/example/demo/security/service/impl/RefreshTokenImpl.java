package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.RefreshToken;
import com.example.demo.security.service.RefreshTokenRepository;
import com.example.demo.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Service

public class RefreshTokenImpl implements RefreshTokenService {



    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

//    @Autowired
//    private AccountService accountService;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void delete(Long id) {
        refreshTokenRepository.deleteById(id);
    }


    public static final long JWT_TOKENRFRESH_VALIDITY = 7 * 24 * 60 * 60;


    @Override
    public RefreshToken createRefreshToken(UserDetails userDetails, Account accountEntity) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + JWT_TOKENRFRESH_VALIDITY * 1000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setAccount(accountEntity);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Account verifyExpiration(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();

            Date expiryDate = refreshToken.getExpiryDate();
            if (expiryDate != null && expiryDate.before(new Date())) {
                refreshTokenRepository.delete(refreshToken);
                return null; // Token đã hết hạn
            }
            return refreshToken.getAccount();
        }
        return null;
    }


    @Override
    public void deleteByAccount(Account accountEntity) {
        refreshTokenRepository.deleteByAccount(accountEntity);
    }


}

