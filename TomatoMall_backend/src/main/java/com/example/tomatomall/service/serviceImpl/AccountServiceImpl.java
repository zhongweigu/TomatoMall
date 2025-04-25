package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.utils.TokenUtil;
import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.AccountResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean register(AccountVO accountVO) {
        Account account = accountRepository.findByUsername(accountVO.getUsername());
        if (account != null) {
            throw TomatoMallException.usernameAlreadyExists();
        }
        String rawPassword = accountVO.getPassword();
        String encoderPassword = passwordEncoder.encode(rawPassword);
        accountVO.setPassword(encoderPassword);
        Account newAccount = accountVO.toPO();
        accountRepository.save(newAccount);
        return true;
    }

    @Override
    public String login(String username, String password) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw TomatoMallException.phoneOrPasswordError();
        }
        if(passwordEncoder.matches(password, account.getPassword())){
            return tokenUtil.getToken(account);
        }else {
            throw TomatoMallException.passwordIncorrectError();
        }
    }

    @Override
    public AccountResponseVO getInformation(String username) {
        Account targetUser = accountRepository.findByUsername(username);
        if(targetUser == null){
            throw TomatoMallException.userDoNotExist();
        }
        return targetUser.toSimpleVO();
    }

    @Override
    public Boolean updateInformation(AccountVO accountVO) {
        Account account=accountRepository.findByUsername(accountVO.getUsername());
        System.out.println(accountVO.getPassword());
        if (accountVO.getPassword()!=null){
            String rawPassword = accountVO.getPassword();
            String encoderPassword = passwordEncoder.encode(rawPassword);
            account.setPassword(encoderPassword);
        }
        if (accountVO.getName()!=null){
            account.setName(accountVO.getName());
        }
        if (accountVO.getLocation()!=null){
            account.setLocation(accountVO.getLocation());
        }
        if (accountVO.getAvatar()!=null){
            account.setAvatar(accountVO.getAvatar());
        }
        if (accountVO.getEmail()!=null){
            account.setEmail(accountVO.getEmail());
        }
        if (accountVO.getTelephone()!=null){
            account.setTelephone(accountVO.getTelephone());
        }
        accountRepository.save(account);
        return true;
    }
}
