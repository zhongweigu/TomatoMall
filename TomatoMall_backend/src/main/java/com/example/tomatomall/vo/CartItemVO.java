package com.example.tomatomall.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Integer cartItemId;  // 使用 Integer 类型
    private Integer productId;   // 使用 Integer 类型
    private String title;
    private String description;
    private String cover;
    private String detail;
    private BigDecimal price;
    private Integer quantity;
}
