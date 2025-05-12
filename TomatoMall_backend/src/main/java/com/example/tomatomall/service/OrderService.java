package com.example.tomatomall.service;

import com.example.tomatomall.enums.OrderStatusEnum;
import com.example.tomatomall.po.Order;
import com.example.tomatomall.vo.CheckoutInfo;
import com.example.tomatomall.vo.CheckoutResponse;

public interface OrderService {
    Order selectByOrderId(String orderId);

    void updateOrderStatus(String orderId, OrderStatusEnum status);

    CheckoutResponse checkout(CheckoutInfo checkoutInfo);
}
