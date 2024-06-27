package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.RefreshToken;
import com.example.demo.security.service.SCRefreshTokenRepository;
import com.example.demo.security.service.SCRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Service

public class SCSCRefreshTokenImpl implements SCRefreshTokenService {



    @Autowired
    private SCRefreshTokenRepository SCRefreshTokenRepository;

//    @Autowired
//    private AccountService accountService;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return SCRefreshTokenRepository.save(refreshToken);
    }

    @Override
    public void delete(Long id) {
        SCRefreshTokenRepository.deleteById(id);
    }


    public static final long JWT_TOKENRFRESH_VALIDITY = 7 * 24 * 60 * 60;


    @Override
    public RefreshToken createRefreshToken(UserDetails userDetails, Account accountEntity) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + JWT_TOKENRFRESH_VALIDITY * 1000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setAccount(accountEntity);
        return SCRefreshTokenRepository.save(refreshToken);
    }

    @Override
    public Account verifyExpiration(String token) {
        Optional<RefreshToken> refreshTokenOptional = SCRefreshTokenRepository.findByToken(token);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();

            Date expiryDate = refreshToken.getExpiryDate();
            if (expiryDate != null && expiryDate.before(new Date())) {
                SCRefreshTokenRepository.delete(refreshToken);
                return null; // Token đã hết hạn
            }
            return refreshToken.getAccount();
        }
        return null;
    }


    @Override
    public void deleteByAccount(Account accountEntity) {
        SCRefreshTokenRepository.deleteByAccount(accountEntity);
    }


}

