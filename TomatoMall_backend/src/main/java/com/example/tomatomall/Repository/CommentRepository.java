package com.example.tomatomall.Repository;

import com.example.tomatomall.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPostId(Integer postId);
    void deleteAllByPostId(Integer postId);
}