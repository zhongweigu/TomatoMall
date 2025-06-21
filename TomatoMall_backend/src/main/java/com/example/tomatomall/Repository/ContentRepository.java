package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    Content findByProductId(Integer productId);
}
