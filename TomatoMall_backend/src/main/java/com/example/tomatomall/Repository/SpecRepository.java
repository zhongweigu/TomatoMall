package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SpecRepository extends JpaRepository<Specification, Integer> {
    Set<Specification> findAllByProduct_Id(int product_id);
}
