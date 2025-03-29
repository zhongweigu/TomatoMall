package com.example.tomatomall.service;

import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.SimpleAccountVO;

public interface AccountService {
    Boolean register(AccountVO userVO);

    String login(String phone,String password);

    SimpleAccountVO getInformation(String username);

    Boolean updateInformation(AccountVO userVO);
}
