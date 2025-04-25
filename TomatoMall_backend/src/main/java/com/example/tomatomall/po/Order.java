package com.example.tomatomall.po;


import com.example.tomatomall.enums.OrderStatusEnum;
import com.example.tomatomall.enums.PaymentEnum;
import com.example.tomatomall.vo.OrderVO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "orderId")
    private Integer orderId;

    @Basic
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal total_amount;

    @Basic
    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentEnum payment_method;

    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "account_id", nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_order_account"))
    private Account account;

    public OrderVO toVO(){
        OrderVO vo = new OrderVO();
        vo.setOrderId(orderId);
        vo.setTotalAmount(total_amount);
        vo.setPaymentMethod(payment_method);
        vo.setPaymentForm(" ");
        vo.setStatus(status);
        vo.setCreateTime(createTime);
        return vo;
    }
}


