package com.example.tomatomall.po;

import com.example.tomatomall.vo.SpecificationVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "specifications")
public class Specification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Length(max = 50)
    @Column(name = "item", nullable = false)
    private String item;

    @Basic
    @Length(max = 255)
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "product_id", nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_specification_product"))
    private Product product;

    public SpecificationVO toVO(){
        SpecificationVO specVO = new SpecificationVO();
        specVO.setItem(item);
        specVO.setValue(value);
        return specVO;
    }
}
