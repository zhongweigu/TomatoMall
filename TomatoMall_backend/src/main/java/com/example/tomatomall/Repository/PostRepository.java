package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}