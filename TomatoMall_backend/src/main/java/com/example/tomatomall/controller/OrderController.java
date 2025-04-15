package com.example.tomatomall.controller;

import com.example.tomatomall.vo.OrderVO;
import com.example.tomatomall.vo.Response;
import org.springframework.web.bind.annotation.*;
import com.example.tomatomall.service.OrderService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 提交订单
     */
    @PostMapping
    public Response<OrderVO> submitOrder(@RequestParam String paymentMethod) {
        OrderVO vo = orderService.createOrder(paymentMethod);
        return Response.buildSuccess(vo);
    }

    /**
     * 获取当前用户的所有订单
     */
    @GetMapping
    public Response<List<OrderVO>> getAllOrders() {
        List<OrderVO> orders = orderService.getUserOrders();
        return Response.buildSuccess(orders);
    }

    /**
     * 根据订单ID获取订单详情
     */
    @GetMapping("/{orderId}")
    public Response<OrderVO> getOrderById(@PathVariable Integer orderId) {
        OrderVO vo = orderService.getOrderById(orderId);
        if (vo != null) {
            return Response.buildSuccess(vo);
        } else {
            return Response.buildFailure("订单不存在", null);
        }
    }

    /**
     * 修改订单支付状态
     */
    @PatchMapping("/{orderId}/status")
    public Response<String> updateOrderStatus(@PathVariable Integer orderId,
                                              @RequestParam String status) {
        boolean success = orderService.updateOrderStatus(orderId, status);
        if (success) {
            return Response.buildSuccess("订单状态更新成功");
        } else {
            return Response.buildFailure("订单不存在或状态更新失败", null);
        }
    }
}
