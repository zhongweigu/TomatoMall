package com.example.tomatomall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderVO {
    private Integer orderId;           // 修改为 Integer 类型
    private Integer userId;            // 修改为 Integer 类型
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;
    private Timestamp createTime;
    private List<CartItemVO> items;    // 订单下的购物车商品列表
}

