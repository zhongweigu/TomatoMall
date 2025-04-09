package com.example.tomatomall.vo;

import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationVO {

    private Integer id;

    private String item;

    private String value;

    private Product product;

    public Specification toPO(){
        Specification spec = new Specification();
        spec.setId(id);
        spec.setItem(item);
        spec.setValue(value);
        spec.setProduct(product);
        return spec;
    }
}
