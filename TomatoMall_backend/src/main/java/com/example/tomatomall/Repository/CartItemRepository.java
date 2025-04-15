package com.example.tomatomall.Repository;

import com.example.tomatomall.po.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    /**
     * 根据用户ID获取购物车商品
     * @param userId 用户ID
     * @return 购物车商品列表
     */
    List<CartItem> findByUserId(Integer userId);

    /**
     * 根据订单ID获取购物车商品
     * @param orderId 订单ID
     * @return 购物车商品列表
     */
    //List<CartItem> findByOrderId(Integer orderId);
}
