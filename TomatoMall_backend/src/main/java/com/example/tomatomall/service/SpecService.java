package com.example.tomatomall.service;

import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;

import java.util.Set;

public interface SpecService {
    Boolean updateSpec(Set<Specification> specifications, int product_id);

    Boolean addSpecs(Set<Specification> specifications, Product product);
}
