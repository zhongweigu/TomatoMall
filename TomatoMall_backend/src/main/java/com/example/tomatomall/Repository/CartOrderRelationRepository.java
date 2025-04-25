package com.example.tomatomall.Repository;

import com.example.tomatomall.po.CartOrderRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartOrderRelationRepository extends JpaRepository<CartOrderRelation, Integer> {

    List<CartOrderRelation> findByOrder_OrderId(Integer orderId);

    Optional<CartOrderRelation> findByCartItem_CartItemId(Integer cartItemId);

    Optional<CartOrderRelation> findByCartItem_CartItemIdAndOrder_OrderId(Integer cartItemId, Integer orderId);
}
