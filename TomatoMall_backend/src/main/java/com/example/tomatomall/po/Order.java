package com.example.tomatomall.po;

import com.example.tomatomall.vo.OrderVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Integer orderId;

    @NotNull
    @Column(name = "userId")
    private Integer userId;

    @NotNull
    @Min(0)
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "status")
    private String status = "PENDING";

    @Column(name = "create_time", insertable = false, updatable = false)
    private Timestamp createTime;

    public OrderVO toVO() {
        OrderVO vo = new OrderVO();
        vo.setOrderId(this.orderId); // Directly assign Integer
        vo.setUserId(this.userId);   // Directly assign Integer
        vo.setTotalAmount(this.totalAmount);
        vo.setPaymentMethod(this.paymentMethod);
        vo.setStatus(this.status);
        vo.setCreateTime(this.createTime);
        return vo;
    }
}