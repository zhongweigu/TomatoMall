package com.example.tomatomall.service;

import com.example.tomatomall.dto.CartItemDTO;
import com.example.tomatomall.po.CartItem;
import com.example.tomatomall.vo.CartItemVO;
import com.example.tomatomall.vo.CartResponseVO;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    CartItemVO addToCart(CartItemDTO cartItemDTO);

    List<CartItemVO> getCartItems();

    boolean deleteCartItem(Integer cartItemId);

    boolean updateCartItemQuantity(Integer cartItemId, Integer quantity);

    BigDecimal calculateTotalAmount(Integer userId);

    List<CartItem> getCartItemsByUserId(Integer userId);

    List<CartItem> getCartItemsByOrderId(Integer orderId);

    CartResponseVO getCartView();

}
