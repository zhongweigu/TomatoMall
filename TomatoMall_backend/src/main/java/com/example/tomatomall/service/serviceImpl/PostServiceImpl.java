package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.*;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Post;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.service.LikeService;
import com.example.tomatomall.service.PostService;
import com.example.tomatomall.service.RateService;
import com.example.tomatomall.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostLikesRepository postLikesRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RateService rateService;

    @Override
    public List<PostResponseVO> getAllPosts() {
        return postRepository.findAll().stream().map(post -> {
            PostResponseVO vo = new PostResponseVO();
            vo.setId(post.getId());
            vo.setCategory(post.getCategory());
            vo.setTitle(post.getTitle());
            vo.setBriefContent(post.getContent().length() > 30
                    ? post.getContent().substring(0, 30) + "......"
                    : post.getContent());
            vo.setImage(post.getImage());
            vo.setCommentsNumber(post.getCommentsNumber());
            vo.setTimeStamp(post.getTimeStamp());
            vo.setNickname(post.getUser().getName());
            vo.setAvatar(post.getUser().getAvatar());
            vo.setUserId(post.getUser().getId());
            vo.setLikes(post.getLikesCount());

            if ("BookComment".equals(post.getCategory())) {
                Product product = productRepository.findById(Integer.parseInt(post.getBook())).orElse(null);
                if (product != null) {
                    vo.setBookId(String.valueOf(product.getId()));
                    vo.setRating(post.getBookRate());
                }

            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public PostDetailResponseVO getPostById(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        PostDetailResponseVO vo = new PostDetailResponseVO();
        vo.setId(post.getId());
        vo.setCategory(post.getCategory());
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setImage(post.getImage());
        vo.setCommentsNumber(post.getCommentsNumber());
        vo.setTimeStamp(post.getTimeStamp());
        vo.setLikesCount(post.getLikesCount());
        vo.setNickname(post.getUser() != null ? post.getUser().getName() : null);
        vo.setRating("BookComment".equals(post.getCategory()) ? post.getBookRate() : null);
        vo.setUserId(post.getUser() != null ? post.getUser().getId() : null);
        vo.setBookId("BookComment".equals(post.getCategory()) ? Integer.valueOf(post.getBook()) : null);
        vo.setAvatar(post.getUser() != null ? post.getUser().getAvatar() : null);

        int userId = getCurrentUserId();
        vo.setLiked(postLikesRepository.existsByUserIdAndPostId(userId, postId));

        return vo;
    }

    @Override
    public String createPost(PostCreateVO postCreateVO) {
        Account user = accountRepository.findById(postCreateVO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setCategory(postCreateVO.getCategory());
        post.setTitle(postCreateVO.getTitle());
        post.setContent(postCreateVO.getContent());
        post.setImage(postCreateVO.getImage());
        post.setUser(user);
        post.setTimeStamp(new Date());
        post.setCommentsNumber(0);
        post.setLikesCount(0);

        if ("BookComment".equals(postCreateVO.getCategory())) {
            post.setBook(postCreateVO.getBookId() != null ? String.valueOf(postCreateVO.getBookId()) : null);
            post.setBookRate(postCreateVO.getRating() != null ? postCreateVO.getRating().doubleValue() : null);

        }

        Post newPost = postRepository.save(post);

        if ("BookComment".equals(postCreateVO.getCategory())) {

            RateCreateVO rateCreateVO = new RateCreateVO();
            rateCreateVO.setPostId(newPost.getId());
            rateCreateVO.setProductId(Integer.valueOf(newPost.getBook()));
            rateCreateVO.setScore(newPost.getBookRate());
            rateCreateVO.setUserId(newPost.getUser().getId());

            rateService.addRate(rateCreateVO);
        }
        return "添加成功！";
    }

    @Override
    public String updatePost(PostUpdateVO postUpdateVO) {
        Post post = postRepository.findById(postUpdateVO.getId())
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        post.setTitle(postUpdateVO.getTitle());
        post.setContent(postUpdateVO.getContent());

        if (postUpdateVO.getImage() != null) {
            post.setImage(postUpdateVO.getImage());
        }

        if ("BookComment".equals(post.getCategory()) && postUpdateVO.getRate() != null) {
            post.setBookRate(postUpdateVO.getRate().doubleValue());
        }

        postRepository.save(post);
        return "更新成功";
    }

    @Autowired
    CommentService commentService;
    @Autowired
    RateRepository rateRepository;

    @Override
    @Transactional
    public String deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 删除与帖子关联的评论
        commentService.deleteCommentsByPostId(postId);

        // 删除与帖子关联的点赞
        postLikesRepository.deleteAllByPostId(postId);

        // 删除评分
        if(post.getCategory().equals("BookComment")){
            rateRepository.deleteByPostId(postId);
        }

        // 删除帖子
        postRepository.delete(post);

        return "删除成功";
    }

    private Integer getCurrentUserId() {
        Account account = (Account) request.getSession().getAttribute("currentUser");
        if (account == null) {
            throw TomatoMallException.notLogin();
        }
        return account.getId();
    }
}