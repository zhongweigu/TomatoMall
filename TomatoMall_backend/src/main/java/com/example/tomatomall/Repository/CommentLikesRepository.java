package com.example.tomatomall.Repository;

import com.example.tomatomall.po.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Integer> {
    boolean existsByUserIdAndCommentId(Integer userId, Integer commentId);
    void deleteByUserIdAndCommentId(Integer userId, Integer commentId);
    Integer countByCommentId(Integer commentId);
    void deleteAllByCommentId(Integer commentId);
}