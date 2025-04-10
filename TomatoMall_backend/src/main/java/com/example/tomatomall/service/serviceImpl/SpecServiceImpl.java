package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.Repository.SpecRepository;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import com.example.tomatomall.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class SpecServiceImpl implements SpecService {
    @Autowired
    SpecRepository specRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Boolean updateSpec(Set<Specification> specifications, int product_id) {


        return true;
    }

    @Override
    public Boolean addSpecs(Set<Specification> specifications, Product product){
        return null;
    }
}
