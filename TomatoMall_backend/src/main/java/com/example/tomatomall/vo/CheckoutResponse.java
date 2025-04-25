package com.example.tomatomall.vo;

import com.example.tomatomall.enums.OrderStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutResponse {
    String orderId;
    String username;
    BigDecimal totalAmount;
    String paymentMethod;
    Date createTime;
    OrderStatusEnum status;
}
