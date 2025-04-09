package com.example.tomatomall.po;

import com.example.tomatomall.vo.StockpileVO;
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
public class Stockpile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 一对一关系，指向产品
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    @NotNull
    private Product product;

    @NotNull
    @Min(0)
    private Integer amount;

    @NotNull
    @Min(0)
    private Integer frozen;

    public StockpileVO toVO() {
        StockpileVO vo = new StockpileVO();
        vo.setId(String.valueOf(this.id));
        vo.setProductId(String.valueOf(this.product.getId()));
        vo.setAmount(this.amount);
        vo.setFrozen(this.frozen);
        return vo;
    }
}
