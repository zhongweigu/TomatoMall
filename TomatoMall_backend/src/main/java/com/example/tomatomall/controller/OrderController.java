package com.example.tomatomall.controller;

import com.example.tomatomall.Repository.CartOrderRelationRepository;
import com.example.tomatomall.Repository.OrderRepository;
import com.example.tomatomall.enums.OrderStatusEnum;
import com.example.tomatomall.po.CartOrderRelation;
import com.example.tomatomall.vo.CartOrderRelationVO;
import com.example.tomatomall.vo.OrderVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.tomatomall.service.OrderService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartOrderRelationRepository cartOrderRelationRepository;


    /**
     * 获取当前用户的所有订单
     */
    @GetMapping
    public Response<List<CartOrderRelationVO>> getAllCartsOrdersRelation() {
        List<CartOrderRelation> orders = cartOrderRelationRepository.findAll();
        List<CartOrderRelationVO> vos = new ArrayList<>();
        for (CartOrderRelation order : orders) {
            CartOrderRelationVO vo = new CartOrderRelationVO();
            vo.setId(order.getId());
            vo.setOrderId(order.getOrder().getOrderId());
            vo.setCartItemId(order.getCartItem().getCartItemId());
            vo.setAccountId(order.getAccount().getId());
            vos.add(vo);
        }
        return Response.buildSuccess(vos);
    }

    /**
     * 根据订单ID获取订单详情
     */
    @GetMapping("/{orderId}")
    public Response<OrderVO> getOrderById(@PathVariable Integer orderId) {
        OrderVO vo = orderRepository.findByOrderId(orderId).toVO();
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
        orderService.updateOrderStatus(orderId.toString(), OrderStatusEnum.valueOf(status));
        return null;
    }
}
