package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findById(int id);
    List<Product> findAll();
}
