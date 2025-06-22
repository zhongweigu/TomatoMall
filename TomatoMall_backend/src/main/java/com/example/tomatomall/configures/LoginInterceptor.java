package com.example.tomatomall.configures;

import com.example.tomatomall.exception.TomatoMallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.tomatomall.utils.TokenUtil;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod(); // 获取请求方法（GET/POST/PUT等）
        String path = request.getRequestURI(); // 获取请求路径

        // 放行 /api/accounts 的所有POST请求
        if ("/api/accounts".equals(path) && "POST".equalsIgnoreCase(method)) {
            return true;
        }

        System.out.println(request.getHeader("token"));
        String token = request.getHeader("token");
        System.out.println(token);
        if (token != null && tokenUtil.verifyToken(token)) {
            request.getSession().setAttribute("currentUser",tokenUtil.getAccount(token));
            return true;
        }else {
            throw TomatoMallException.notLogin();
        }
    }

}
