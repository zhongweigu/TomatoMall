package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.PostRepository;
import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.po.Post;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.service.PostService;
import com.example.tomatomall.vo.PostResponseVO;
import com.example.tomatomall.vo.PostDetailResponseVO;
import com.example.tomatomall.vo.PostCreateVO;
import com.example.tomatomall.vo.PostUpdateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

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
            if ("BookCommnet".equals(post.getCategory())) {
                vo.setBook(post.getBook());
                vo.setBookRate(post.getBookRate());
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
        vo.setUserId(post.getUser() != null ? post.getUser().getId() : null);
        vo.setBookId("BookCommnet".equals(post.getCategory()) ? post.getId() : null);
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

        if ("BookCommnet".equals(postCreateVO.getCategory())) {
            post.setBook(postCreateVO.getBookId() != null ? String.valueOf(postCreateVO.getBookId()) : null);
            post.setBookRate(postCreateVO.getRate() != null ? postCreateVO.getRate().doubleValue() : null);
        }

        postRepository.save(post);
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

        if ("BookCommnet".equals(post.getCategory()) && postUpdateVO.getRate() != null) {
            post.setBookRate(postUpdateVO.getRate().doubleValue());
        }

        postRepository.save(post);
        return "更新成功";
    }

    @Override
    public String deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 删除与帖子关联的评论
        //commentRepository.deleteByPostId(postId);

        // 删除与帖子关联的点赞
        //likeRepository.deleteByPostId(postId);

        // 删除帖子
        postRepository.delete(post);

        return "删除成功";
    }
}