package com.example.tomatomall.vo;

import com.example.tomatomall.enums.OrderStatusEnum;
import com.example.tomatomall.enums.PaymentEnum;
import com.example.tomatomall.po.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OrderVO {
    private Integer orderId;
    private BigDecimal totalAmount;
    private PaymentEnum paymentMethod;
    private String paymentForm;
    private OrderStatusEnum status;
    private Date createTime;
}
