package com.example.tomatomall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemDTO {
    @JsonProperty("productId")
    private Integer productId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("cartItemId")
    private Integer cartItemId;
}