package com.example.tomatomall.controller;

import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.Response;
import com.example.tomatomall.vo.SimpleAccountVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Resource
    AccountService accountService;

    /**
     * 更新用户信息
     */
    @PostMapping()
    public Response<Boolean> updateInformation(@RequestBody AccountVO accountVO){
        return Response.buildSuccess(accountService.updateInformation(accountVO));
    }
    /**
     * 创建新的用户
     */
    @PostMapping("/register")
    public Response<String> register(@RequestBody AccountVO userVO){
        return Response.buildSuccess(accountService.register(userVO) ? "注册成功" :null);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<String> login(@RequestParam("username") String username, @RequestParam("password") String password){
        System.out.println(username);
        return Response.buildSuccess(accountService.login(username, password));
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{username}")
    public Response<SimpleAccountVO> getInformation(@PathVariable String username){
        return Response.buildSuccess(accountService.getInformation(username));
    }
}
