package com.example.tomatomall.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.po.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

@Component
public class TokenUtil {
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    @Autowired
    AccountRepository accountRepository;

    public String getToken(Account account) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        System.out.println(account.getId());
        return JWT.create()
                .withAudience(String.valueOf(account.getId()))
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(account.getPassword()));
    }

    public boolean verifyToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Integer accountId=Integer.parseInt(decodedJWT.getAudience().get(0));
            System.out.println(accountId);
            Account account= accountRepository.findById(accountId).get();
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Account getAccount(String token){
        Integer accountId=Integer.parseInt(JWT.decode(token).getAudience().get(0));
        return accountRepository.findById(accountId).get();
    }
}
