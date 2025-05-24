package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.CommentLikesRepository;
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

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    CommentLikesRepository commentLikesRepository;

    @Autowired
    private HttpServletRequest request;

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
        comment.setLikesCount(0);

        commentRepository.save(comment);

        // 更新帖子评论数
        post.setCommentsNumber(post.getCommentsNumber() + 1);
        postRepository.save(post);

        return "评论成功！";
    }

    @Override
    @Transactional
    public String deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TomatoMallException("400", "评论不存在"));
        Post post = comment.getPost();
        // 删除该评论的点赞
        commentLikesRepository.deleteAllByCommentId(commentId);

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

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        for (Comment comment : comments) {
            deleteComment(comment.getId());
        }

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
            vo.setUserId(comment.getUser().getId());
            vo.setPostId(comment.getPost().getId());
            vo.setLikesCount(comment.getLikesCount());

            int userId = getCurrentUserId();
            vo.setLiked(commentLikesRepository.existsByUserIdAndCommentId(userId, comment.getId()));

            return vo;
        }).collect(Collectors.toList());
    }

    private Integer getCurrentUserId() {
        Account account = (Account) request.getSession().getAttribute("currentUser");
        if (account == null) {
            throw TomatoMallException.notLogin();
        }
        return account.getId();
    }
}