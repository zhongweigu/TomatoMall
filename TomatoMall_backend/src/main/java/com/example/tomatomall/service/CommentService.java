package com.example.tomatomall.service;

import com.example.tomatomall.vo.CommentCreateVO;
import com.example.tomatomall.vo.CommentResponseVO;

import java.util.List;

public interface CommentService {
    String addComment(CommentCreateVO commentCreateVO);
    String deleteComment(Integer commentId);
    String deleteCommentsByPostId(Integer postId);
    String updateComment(Integer id, String content, String image);
    List<CommentResponseVO> getCommentsByPostId(Integer postId);
}