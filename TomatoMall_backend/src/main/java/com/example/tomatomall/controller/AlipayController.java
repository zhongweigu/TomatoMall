package com.example.tomatomall.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.tomatomall.Repository.OrderRepository;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Order;
import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.utils.AlipayUtil;
import com.example.tomatomall.vo.OrderVO;
import com.example.tomatomall.vo.Response;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alipay.api.AlipayConstants.FORMAT;

@RestController
@RequestMapping("/api")
public class AlipayController {

    @Autowired
    AlipayUtil alipayUtil;

    @Resource
    OrderService orderService;

    @PostMapping("/orders/{orderId}/pay")
    public void pay(@PathVariable("orderId") String orderId, HttpServletResponse httpResponse) throws Exception {
        Order order = orderService.selectByOrderId(orderId);

        if (order == null) {
            throw TomatoMallException.orderDoNotExist();
        }

        AlipayClient alipayClient = new DefaultAlipayClient(alipayUtil.getServerUrl(), alipayUtil.getAppId(), alipayUtil.getMerchantPrivateKey(), FORMAT, alipayUtil.getCharset(),
                alipayUtil.getAlipayPublicKey(), alipayUtil.getSignType());
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        request.setNotifyUrl(alipayUtil.getNotifyUrl());

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getOrderId().toString());          // 商户订单号
        bizContent.put("total_amount", order.getTotal_amount().toString()); // 金额
        bizContent.put("subject", "订单支付-" + orderId);    // 订单标题
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY"); // 销售产品码
        System.out.println("bizContent: " + bizContent.toString());

        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();

        httpResponse.setContentType("text/html;charset=utf-8");
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();

    }

    @PostMapping("/alipay/notify")
    public void handleAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        // 1. 解析支付宝回调参数（通常是 application/x-www-form-urlencoded）
        Map<String, String> params = request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

        // 2. 验证支付宝签名（防止伪造请求）
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayUtil.getAlipayPublicKey(), "UTF-8", "RSA2");
        if (!signVerified) {
            response.getWriter().print("fail"); // 签名验证失败，返回 fail
            return;
        }

        // 3. 处理业务逻辑（更新订单、减库存等）
        String tradeStatus = params.get("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            String orderId = params.get("out_trade_no"); // 您的订单号
            String alipayTradeNo = params.get("trade_no"); // 支付宝交易号
            String amount = params.get("total_amount"); // 支付金额

            // 更新订单状态（注意幂等性，防止重复处理）
            orderService.updateOrderStatus(orderId, alipayTradeNo, amount);

            // 扣减库存（建议加锁或乐观锁）
//            inventoryService.reduceStock(orderId);
        }

        // 4. 必须返回纯文本的 "success"（支付宝要求）
        response.getWriter().print("success");
    }
}
