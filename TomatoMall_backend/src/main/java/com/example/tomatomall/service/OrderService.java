package com.example.tomatomall.service;

import com.example.tomatomall.vo.OrderVO;

import java.util.List;

public interface OrderService {

    /**
     * 创建订单
     * @param paymentMethod 支付方式
     * @return 订单VO
     */
    OrderVO createOrder(String paymentMethod);

    /**
     * 获取当前用户的所有订单
     * @return 订单列表
     */
    List<OrderVO> getUserOrders();

    /**
     * 根据订单ID获取订单详情
     * @param orderId 订单ID
     * @return 订单详情VO
     */
    OrderVO getOrderById(Integer orderId);

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新的订单状态
     * @return 更新结果
     */
    boolean updateOrderStatus(Integer orderId, String status);
}

