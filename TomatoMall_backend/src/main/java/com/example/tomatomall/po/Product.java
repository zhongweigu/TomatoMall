package com.example.tomatomall.po;


import com.example.tomatomall.enums.TypeEnum;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.SpecificationVO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
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
    @JsonManagedReference
    private Set<Specification> specifications;


    public void addSpecification(Specification spec) {
        if (specifications == null) {
            specifications = new HashSet<>();
        }
        spec.setProduct(this);
        specifications.add(spec);
         // 关键：设置子对父的引用
    }

    public void removeSpecification(Specification spec) {
        specifications.remove(spec);
        spec.setProduct(null); // 清理引用
    }

    public ProductVO toVO() {
        ProductVO productVO = new ProductVO();
        productVO.setId(this.id);
        productVO.setTitle(this.title);
        productVO.setPrice(this.price);
        productVO.setRate(this.rate);
        productVO.setDescription(this.description);
        productVO.setCover(this.cover);
        productVO.setDetail(this.detail);
        productVO.setType(this.type);

        if(specifications!=null){
            Set<SpecificationVO> specificationVOS = new HashSet<>();
            for (Specification spec : specifications){
                SpecificationVO specificationVO = new SpecificationVO();
                specificationVO.setId(spec.getId());
                specificationVO.setItem(spec.getItem());
                specificationVO.setValue(spec.getValue());
                specificationVO.setProduct_id(spec.getProduct().getId());
                specificationVOS.add(specificationVO);
            }
            productVO.setSpecifications(specificationVOS);
        }
        return productVO;
    }
}