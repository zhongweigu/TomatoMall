package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.po.Order;
import com.example.tomatomall.Repository.OrderRepository;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.vo.CartItemVO;
import com.example.tomatomall.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Override
    public OrderVO createOrder(String paymentMethod) {
        Integer userId = getCurrentUserId();

        BigDecimal totalAmount = cartService.calculateTotalAmount(userId);

        Order order = new Order();
        order.setUserId(userId);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        List<CartItemVO> cartItems = cartService.getCartItemsByUserId(userId).stream()
                .map(cartItem -> cartItem.toVO()) // 使用 toVO 方法
                .collect(Collectors.toList());

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(order.getOrderId());
        orderVO.setUserId(userId);
        orderVO.setTotalAmount(order.getTotalAmount());
        orderVO.setPaymentMethod(order.getPaymentMethod());
        orderVO.setStatus(order.getStatus());
        orderVO.setItems(cartItems);

        return orderVO;
    }

    @Override
    public List<OrderVO> getUserOrders() {
        Integer userId = getCurrentUserId();

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderId(order.getOrderId());
            orderVO.setUserId(order.getUserId());
            orderVO.setTotalAmount(order.getTotalAmount());
            orderVO.setPaymentMethod(order.getPaymentMethod());
            orderVO.setStatus(order.getStatus());
            return orderVO;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderVO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(order.getOrderId());
        orderVO.setUserId(order.getUserId());
        orderVO.setTotalAmount(order.getTotalAmount());
        orderVO.setPaymentMethod(order.getPaymentMethod());
        orderVO.setStatus(order.getStatus());

        List<CartItemVO> cartItems = cartService.getCartItemsByOrderId(orderId).stream()
                .map(cartItem -> cartItem.toVO()) // 使用 toVO 方法
                .collect(Collectors.toList());

        orderVO.setItems(cartItems);
        return orderVO;
    }

    @Override
    public boolean updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }

        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }

    private Integer getCurrentUserId() {
        return 1; // 示例实现
    }
}