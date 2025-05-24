package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Integer> {
    void deleteByPostId(Integer postId);
    List<Rate> findAllByPostId(Integer postId);
    List<Rate> findAllByProductId(Integer productId);

    Rate findByPostId(Integer postId);

    boolean existsByPostIdAndUserId(Integer postId, Integer userId);
}