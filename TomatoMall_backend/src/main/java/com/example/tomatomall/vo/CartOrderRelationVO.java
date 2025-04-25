package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartOrderRelationVO {
    private Integer id;
    private Integer orderId;
    private Integer cartItemId;
}
