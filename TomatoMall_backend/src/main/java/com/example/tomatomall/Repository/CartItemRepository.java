package com.example.tomatomall.Repository;

import com.example.tomatomall.po.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUserId(Integer userId);

    CartItem findByCartItemId(Integer cartItemId);
}
