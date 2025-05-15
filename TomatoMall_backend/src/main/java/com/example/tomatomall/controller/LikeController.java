package com.example.tomatomall.controller;

import com.example.tomatomall.service.LikeService;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forum/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public Response<String> addLike(@RequestParam Integer userId,
                                    @RequestParam(required = false) Integer postId,
                                    @RequestParam(required = false) Integer commentId) {
        try {
            return Response.buildSuccess(likeService.addLike(userId, postId, commentId));
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public Response<String> removeLike(@RequestParam Integer userId,
                                       @RequestParam(required = false) Integer postId,
                                       @RequestParam(required = false) Integer commentId) {
        try {
            return Response.buildSuccess(likeService.removeLike(userId, postId, commentId));
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @GetMapping
    public Response<Integer> countLikes(@RequestParam(required = false) Integer postId,
                                        @RequestParam(required = false) Integer commentId) {
        try {
            if (postId != null) {
                return Response.buildSuccess(likeService.countLikes(postId, null));
            } else if (commentId != null) {
                return Response.buildSuccess(likeService.countLikes(null, commentId));
            } else {
                throw new IllegalArgumentException("post_id 和 comment_id 不能同时为空！");
            }
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public Response<String> deleteLikesByPostOrCommentId(@RequestParam(required = false) Integer postId,
                                                         @RequestParam(required = false) Integer commentId) {
        try {
            if (postId != null) {
                return Response.buildSuccess(likeService.deleteLikesByPostOrCommentId(postId, null));
            } else if (commentId != null) {
                return Response.buildSuccess(likeService.deleteLikesByPostOrCommentId(null, commentId));
            } else {
                throw new IllegalArgumentException("post_id 和 comment_id 不能同时为空！");
            }
        } catch (RuntimeException e) {
            return Response.buildFailure("400", e.getMessage());
        }
    }
}