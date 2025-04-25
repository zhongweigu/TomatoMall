package com.example.tomatomall.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carts_orders_relation")
public class CartOrderRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartitem_id", referencedColumnName = "cartItemId",
            foreignKey = @ForeignKey(name = "fk_relation_cartitem"))
    private CartItem cartItem;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "orderId",
            foreignKey = @ForeignKey(name = "fk_relation_order"))
    private Order order;
}
