package com.example.tomatomall.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponseVO {
    private List<CartItemVO> items;
    private Integer total;         // 商品种类数量
    private BigDecimal totalAmount; // 总金额
}

