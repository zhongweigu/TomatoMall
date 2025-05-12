package com.example.tomatomall.controller;

import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.Response;
import com.example.tomatomall.vo.AccountResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Resource
    AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    /**
     * 更新用户信息
     */
    @PutMapping()
    public Response<Boolean> updateInformation(@RequestBody AccountVO accountVO){
        return Response.buildSuccess(accountService.updateInformation(accountVO));
    }
    /**
     * 创建新的用户
     */
    @PostMapping()
    public Response<String> register(@RequestBody AccountVO accountVO){
        return Response.buildSuccess(accountService.register(accountVO) ? "注册成功" :null);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<String> login(@RequestBody AccountVO accountVO){
        String rawPassword = accountVO.getPassword();
        String username = accountVO.getUsername();
        System.out.println(username);
        return Response.buildSuccess(accountService.login(username, rawPassword));
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{username}")
    public Response<AccountResponseVO> getInformation(@PathVariable String username){
        return Response.buildSuccess(accountService.getInformation(username));
    }

}
