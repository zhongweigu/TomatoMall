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

    private Integer product_id;

    public Specification toPO(){
        Specification spec = new Specification();
        spec.setId(this.id);
        spec.setItem(this.item);
        spec.setValue(this.value);
        return spec;
    }
}
