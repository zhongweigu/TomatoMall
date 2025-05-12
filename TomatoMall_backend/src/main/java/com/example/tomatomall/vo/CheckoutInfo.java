package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutInfo {
    List<String> cartItemIds;
    ShippingAddress shippingAddress;
    String paymentMethod;
}
