package com.example.tomatomall.service;

import com.example.tomatomall.vo.PostResponseVO;
import com.example.tomatomall.vo.PostDetailResponseVO;
import com.example.tomatomall.vo.PostCreateVO;
import com.example.tomatomall.vo.PostUpdateVO;

import java.util.List;

public interface PostService {
    List<PostResponseVO> getAllPosts();
    PostDetailResponseVO getPostById(Integer postId);
    String createPost(PostCreateVO postCreateVO);
    String updatePost(PostUpdateVO postUpdateVO);
    String deletePost(Integer postId);
}