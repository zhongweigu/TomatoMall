package com.example.tomatomall.vo;

import com.example.tomatomall.enums.TypeEnum;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    private List<Specification> specifications;

    public Product toPO(){
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setRate(rate);
        product.setDescription(description);
        product.setCover(cover);
        product.setDetail(detail);
        product.setType(type);
        product.setSpecifications(specifications);
        return product;
    }


}
