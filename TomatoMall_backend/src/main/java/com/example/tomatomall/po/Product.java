package com.example.tomatomall.po;


import com.example.tomatomall.enums.TypeEnum;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.SpecificationVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Length(max = 50)
    @Column(name = "title", nullable = false)
    private String title;

    @Basic
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Basic
    @Column(name = "rate", nullable = false)
    private Double rate;

    @Basic
    @Length(max = 255)
    @Column(name = "description")
    private String description;

    @Basic
    @Length(max = 500)
    @Column(name = "cover")
    private String cover;

    @Basic
    @Length(max = 500)
    @Column(name = "detail")
    private String detail;

    // 新增类型type,用于前端分类
    @Basic
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Specification> specifications;

    public ProductVO toVO() {
        ProductVO productVO = new ProductVO();
        productVO.setTitle(this.title);
        productVO.setPrice(this.price);
        productVO.setRate(this.rate);
        productVO.setDescription(this.description);
        productVO.setCover(this.cover);
        productVO.setDetail(this.detail);
        productVO.setType(this.type);
        // 转换 Specifications → SpecificationVO（避免循环引用）
        if (this.specifications != null) {
            Set<SpecificationVO> specVOs = this.specifications.stream()
                    .map(spec -> {
                        SpecificationVO specVO = new SpecificationVO();
                        specVO.setId(spec.getId());
                        specVO.setItem(spec.getItem());
                        specVO.setValue(spec.getValue());
                        // 不设置 specVO.setProduct()，避免循环引用
                        return specVO;
                    })
                    .collect(Collectors.toSet());
            productVO.setSpecifications(specVOs);
        }
        return productVO;
    }
}