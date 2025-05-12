package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.CommentRepository;
import com.example.tomatomall.Repository.PostRepository;
import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Comment;
import com.example.tomatomall.po.Post;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.vo.CommentCreateVO;
import com.example.tomatomall.vo.CommentResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public String addComment(CommentCreateVO commentCreateVO) {
        Post post = postRepository.findById(commentCreateVO.getPostId())
                .orElseThrow(() -> TomatoMallException.productDoNotExist());
        Account user = accountRepository.findById(commentCreateVO.getUserId())
                .orElseThrow(() -> TomatoMallException.userDoNotExist());

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentCreateVO.getContent());
        comment.setImage(commentCreateVO.getImage());

        commentRepository.save(comment);

        // 更新帖子评论数
        post.setCommentsNumber(post.getCommentsNumber() + 1);
        postRepository.save(post);

        return "评论成功！";
    }

    @Override
    public String deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TomatoMallException("400", "评论不存在"));
        Post post = comment.getPost();

        commentRepository.delete(comment);

        // 更新帖子评论数
        post.setCommentsNumber(post.getCommentsNumber() - 1);
        postRepository.save(post);

        return "删除成功";
    }

    @Override
    @Transactional
    public String deleteCommentsByPostId(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        commentRepository.deleteAllByPostId(postId);

        // 更新帖子评论数
        post.setCommentsNumber(0);
        postRepository.save(post);

        return "删除该帖的所有评论成功";
    }

    @Override
    public String updateComment(Integer id, String content, String image) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> TomatoMallException.commentDoNotExist());

        comment.setContent(content);
        if (image != null) {
            comment.setImage(image);
        }

        commentRepository.save(comment);
        return "更新成功";
    }

    @Override
    public List<CommentResponseVO> getCommentsByPostId(Integer postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        return comments.stream().map(comment -> {
            CommentResponseVO vo = new CommentResponseVO();
            vo.setId(comment.getId());
            vo.setContent(comment.getContent());
            vo.setImage(comment.getImage());
            vo.setTimeStamp(comment.getCreatedAt());
            vo.setNickname(comment.getUser().getName());
            vo.setAvatar(comment.getUser().getAvatar());
            return vo;
        }).collect(Collectors.toList());
    }
}