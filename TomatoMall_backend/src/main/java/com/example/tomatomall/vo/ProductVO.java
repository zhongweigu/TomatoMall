package com.example.tomatomall.vo;

import com.example.tomatomall.enums.TypeEnum;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {

    private Integer id;

    private String title;

    private BigDecimal price;

    private Double rate;

    private String description;

    private String cover;

    private String detail;

    private TypeEnum type;

    private Set<SpecificationVO> specifications;

    public Product toPO(){
        Product product = new Product();
        product.setId(this.id);
        product.setTitle(this.title);
        product.setPrice(this.price);
        product.setRate(this.rate);
        product.setDescription(this.description);
        product.setCover(this.cover);
        product.setDetail(this.detail);
        product.setType(this.type);
        return product;
    }



}
