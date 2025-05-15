package com.example.tomatomall.controller;

import com.example.tomatomall.service.PostService;
import com.example.tomatomall.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public Response<List<PostResponseVO>> getAllPosts() {
        return Response.buildSuccess(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public Response<PostDetailResponseVO> getPostById(@PathVariable Integer postId) {
        return Response.buildSuccess(postService.getPostById(postId));
    }

    @PostMapping
    public Response<String> createPost(@RequestBody PostCreateVO postCreateVO) {
        return Response.buildSuccess(postService.createPost(postCreateVO));
    }

    @PutMapping
    public Response<String> updatePost(@RequestBody PostUpdateVO postUpdateVO) {
        try {
            return Response.buildSuccess(postService.updatePost(postUpdateVO));
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public Response<String> deletePost(@PathVariable Integer postId) {
        try {
            return Response.buildSuccess(postService.deletePost(postId));
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }
}