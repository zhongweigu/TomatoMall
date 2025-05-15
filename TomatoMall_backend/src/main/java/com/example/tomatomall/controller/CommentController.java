package com.example.tomatomall.controller;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.vo.CommentCreateVO;
import com.example.tomatomall.vo.CommentResponseVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Response<String> addComment(@RequestBody CommentCreateVO commentCreateVO) {
        try {
            return Response.buildSuccess(commentService.addComment(commentCreateVO));
        } catch (TomatoMallException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    @DeleteMapping("/{commentId}")
    public Response<String> deleteComment(@PathVariable Integer commentId) {
        try {
            return Response.buildSuccess(commentService.deleteComment(commentId));
        } catch (TomatoMallException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    @DeleteMapping("/post/{postId}")
    public Response<String> deleteCommentsByPostId(@PathVariable Integer postId) {
        return Response.buildSuccess(commentService.deleteCommentsByPostId(postId));
    }

    @PutMapping
    public Response<String> updateComment(@RequestParam Integer id,
                                          @RequestParam String content,
                                          @RequestParam(required = false) String image) {
        try {
            return Response.buildSuccess(commentService.updateComment(id, content, image));
        } catch (TomatoMallException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    @GetMapping("/{postId}")
    public Response<List<CommentResponseVO>> getCommentsByPostId(@PathVariable Integer postId) {
        return Response.buildSuccess(commentService.getCommentsByPostId(postId));
    }
}