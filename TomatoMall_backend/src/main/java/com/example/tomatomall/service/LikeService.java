package com.example.tomatomall.service;

public interface LikeService {
    String addLike(Integer userId, Integer postId, Integer commentId);
    String removeLike(Integer userId, Integer postId, Integer commentId);
    Integer countLikes(Integer postId, Integer commentId);
    String deleteLikesByPostOrCommentId(Integer postId, Integer commentId);
}