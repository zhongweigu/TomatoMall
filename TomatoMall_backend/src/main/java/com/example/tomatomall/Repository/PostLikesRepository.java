package com.example.tomatomall.Repository;

import com.example.tomatomall.po.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikesRepository extends JpaRepository<PostLikes, Integer> {
    boolean existsByUserIdAndPostId(Integer userId, Integer postId);
    void deleteByUserIdAndPostId(Integer userId, Integer postId);
    Integer countByPostId(Integer postId);
    void deleteAllByPostId(Integer postId);
}