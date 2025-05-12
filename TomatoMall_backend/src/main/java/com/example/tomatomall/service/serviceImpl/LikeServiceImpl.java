package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.PostLikesRepository;
import com.example.tomatomall.Repository.CommentLikesRepository;
import com.example.tomatomall.po.PostLikes;
import com.example.tomatomall.po.CommentLikes;
import com.example.tomatomall.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private PostLikesRepository postLikesRepository;

    @Autowired
    private CommentLikesRepository commentLikesRepository;

    @Override
    public String addLike(Integer userId, Integer postId, Integer commentId) {
        if (postId != null) {
            if (postLikesRepository.existsByUserIdAndPostId(userId, postId)) {
                throw new RuntimeException("已点赞该帖子！");
            }
            PostLikes like = new PostLikes();
            like.setUserId(userId);
            like.setPostId(postId);
            postLikesRepository.save(like);
        } else if (commentId != null) {
            if (commentLikesRepository.existsByUserIdAndCommentId(userId, commentId)) {
                throw new RuntimeException("已点赞该评论！");
            }
            CommentLikes like = new CommentLikes();
            like.setUserId(userId);
            like.setCommentId(commentId);
            commentLikesRepository.save(like);
        } else {
            throw new IllegalArgumentException("post_id 和 comment_id 不能同时为空！");
        }
        return "点赞成功！";
    }

    @Override
    @Transactional
    public String removeLike(Integer userId, Integer postId, Integer commentId) {
        if (postId != null) {
            postLikesRepository.deleteByUserIdAndPostId(userId, postId);
        } else if (commentId != null) {
            commentLikesRepository.deleteByUserIdAndCommentId(userId, commentId);
        } else {
            throw new IllegalArgumentException("post_id 和 comment_id 不能同时为空！");
        }
        return "取消点赞成功";
    }

    @Override
    public Integer countLikes(Integer postId, Integer commentId) {
        if (postId != null) {
            return postLikesRepository.countByPostId(postId);
        } else if (commentId != null) {
            return commentLikesRepository.countByCommentId(commentId);
        } else {
            throw new IllegalArgumentException("post_id 和 comment_id 不能同时为空！");
        }
    }

    @Override
    public String deleteLikesByPostOrCommentId(Integer postId, Integer commentId) {
        if (postId != null) {
            postLikesRepository.deleteAllByPostId(postId);
        } else if (commentId != null) {
            commentLikesRepository.deleteAllByCommentId(commentId);
        } else {
            throw new IllegalArgumentException("post_id 和 comment_id 不能同时为空！");
        }
        return "删除相关点赞成功！";
    }
}