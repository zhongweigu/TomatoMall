package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.Repository.CartItemRepository;
import com.example.tomatomall.Repository.CartOrderRelationRepository;
import com.example.tomatomall.Repository.OrderRepository;
import com.example.tomatomall.enums.OrderStatusEnum;
import com.example.tomatomall.enums.PaymentEnum;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.*;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.vo.CartResponseVO;
import com.example.tomatomall.vo.CheckoutInfo;
import com.example.tomatomall.vo.CheckoutResponse;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartOrderRelationRepository cartOrderRelationRepository;

    @Override
    public Order selectByOrderId(String orderId) {
        return orderRepository.findByOrderId(Integer.parseInt(orderId));
    }

    @Override
    public void updateOrderStatus(String orderId, OrderStatusEnum status) {
        Order order = orderRepository.findByOrderId(Integer.parseInt(orderId));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public CheckoutResponse checkout(CheckoutInfo checkoutInfo) {

        Integer userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in");
        }

        Optional<Account> optionalAccount = accountRepository.findById(userId);
        if (!optionalAccount.isPresent()) {
            throw new IllegalStateException("User does not exist");
        }
        Account account = optionalAccount.get();

        Order order = new Order();
        order.setAccount(account);
        order.setStatus(OrderStatusEnum.PENDING);
        order.setCreateTime(new Date());
        order.setPayment_method(PaymentEnum.Alipay);
        order.setTotal_amount(calculateTotalAmount(checkoutInfo.getCartItemIds()));
        Order savedOrder = orderRepository.save(order);

        List<String> containedIds = new ArrayList<>();
        for (String cartItemId : checkoutInfo.getCartItemIds()){
            if(containedIds.contains(cartItemId)){
                continue;
            }
            containedIds.add(cartItemId);
            CartOrderRelation cartOrderRelation = new CartOrderRelation();
            cartOrderRelation.setCartItem(cartItemRepository.findByCartItemId(Integer.parseInt(cartItemId)));
            cartOrderRelation.setOrder(savedOrder);
            cartOrderRelation.setAccount(account);
            cartOrderRelationRepository.save(cartOrderRelation);
        }

        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setUsername(savedOrder.getAccount().getUsername());
        checkoutResponse.setOrderId(savedOrder.getOrderId().toString());
        checkoutResponse.setPaymentMethod(String.valueOf(savedOrder.getPayment_method()));
        checkoutResponse.setCreateTime(savedOrder.getCreateTime());
        checkoutResponse.setStatus(savedOrder.getStatus());
        checkoutResponse.setTotalAmount(savedOrder.getTotal_amount());
        return checkoutResponse;
    }

    private Integer getCurrentUserId() {
        Account account = (Account) request.getSession().getAttribute("currentUser");
        if (account == null) {
            throw TomatoMallException.notLogin();
        }
        return account.getId();
    }

    private BigDecimal calculateTotalAmount(List<String> cartItemIds) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (String cartItemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(Integer.parseInt(cartItemId)).get();
            Product product = cartItem.getProduct();
            BigDecimal productPrice = product.getPrice();
            totalAmount = totalAmount.add(productPrice);
        }
        return totalAmount;
    }
}
