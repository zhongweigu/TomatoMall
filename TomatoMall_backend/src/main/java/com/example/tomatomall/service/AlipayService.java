package com.example.tomatomall.service;

import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AlipayService {
    String pay(String out_trade_no, HttpServletResponse httpResponse)throws IOException, AlipayApiException;
    void notify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException;
}
