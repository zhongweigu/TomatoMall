package com.example.tomatomall.po;

import com.example.tomatomall.enums.OrderStatusEnum;
import com.example.tomatomall.vo.CartItemVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private Integer cartItemId;

    // user 字段是 Account 类型，而不是 Integer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_cartitem_user"))
    @JsonBackReference
    @NotNull
    private Account user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_cartitem_product"))
    @JsonBackReference
    @NotNull
    private Product product;

    @NotNull
    @Min(1)
    private Integer quantity;

    private OrderStatusEnum status;

    public Integer getProductId() {
        return product != null ? product.getId() : null;
    }

    // getters and setters for user, product, quantity

    public CartItemVO toVO() {
        CartItemVO vo = new CartItemVO();
        vo.setCartItemId(this.cartItemId);
        vo.setProductId(this.product.getId());
        vo.setTitle(this.product.getTitle());
        vo.setPrice(this.product.getPrice());
        vo.setDescription(this.product.getDescription());
        vo.setCover(this.product.getCover());
        vo.setDetail(this.product.getDetail());
        vo.setQuantity(this.quantity);
        vo.setUserId(this.user.getId());
        vo.setStatus(this.status);
        return vo;
    }
}
