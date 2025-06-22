package com.example.tomatomall.controller;

import com.alipay.api.AlipayApiException;
import com.example.tomatomall.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AlipayController {

    @Autowired
    AlipayService alipayService;

    @PostMapping("/orders/{orderId}/pay")
    public void pay(@PathVariable("orderId") String orderId, HttpServletResponse httpResponse) throws AlipayApiException, IOException {
        alipayService.pay(orderId, httpResponse);
    }

    @PostMapping("/alipay/notify")
    public void handleAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        alipayService.notify(request, response);
    }
}
