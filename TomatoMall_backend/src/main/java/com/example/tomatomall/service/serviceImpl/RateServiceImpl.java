package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.AccountRepository;
import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.Repository.PostRepository;
import com.example.tomatomall.Repository.RateRepository;
import com.example.tomatomall.po.Account;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Post;
import com.example.tomatomall.po.Rate;
import com.example.tomatomall.service.RateService;
import com.example.tomatomall.vo.RateCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public String addRate(RateCreateVO rateCreateVO) {
        // 参数校验
        if (rateCreateVO.getUserId() == null) {
            throw new IllegalArgumentException("用户 ID 不能为空！");
        }
        if (rateCreateVO.getProductId() == null) {
            throw new IllegalArgumentException("书籍 ID 不能为空！");
        }
        if (rateCreateVO.getPostId() == null) {
            throw new IllegalArgumentException("帖子 ID 不能为空！");
        }

        // 验证用户是否存在
        Account user = accountRepository.findById(rateCreateVO.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证书籍是否存在
        Product product = productRepository.findById(rateCreateVO.getProductId())
                .orElseThrow(() -> new RuntimeException("书籍不存在"));

        // 验证帖子是否存在
        Post post = postRepository.findById(rateCreateVO.getPostId())
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 验证用户是否已经评价过该帖子
        if (rateRepository.existsByPostIdAndUserId(rateCreateVO.getPostId(), rateCreateVO.getUserId())) {
            throw new RuntimeException("用户已经评价过该帖子！");
        }

        // 创建评分记录
        Rate rate = new Rate();
        rate.setUser(user);
        rate.setProduct(product);
        rate.setPost(post);
        rate.setScore(rateCreateVO.getScore());

        // 保存评分
        rateRepository.save(rate);

        // 更新书籍的总评价
        updateBookRate(rateCreateVO.getProductId());

        return "评价成功！";
    }

    @Override
    public void deleteRate(Integer id) {
        Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评价不存在"));
        Integer productId = rate.getProduct().getId();
        rateRepository.delete(rate);

        // 更新书籍的总评价
        updateBookRate(productId);
    }

    @Override
    @Transactional
    public void deleteRatesByPostId(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("帖子不存在");
        }
        Integer productId = Integer.valueOf(postRepository.findById(postId).get().getBook());
        rateRepository.deleteByPostId(postId);

        // 更新书籍的总评价
        updateBookRate(productId);
    }

    @Override
    public String getRate(Integer postId) {
        Rate rate = rateRepository.findByPostId(postId);
        return rate.getScore().toString();
    }

    private void updateBookRate(Integer productId) {
        // 查询所有与该书籍相关的评价
        List<Rate> rates = rateRepository.findAllByProductId(productId);

        // 计算平均值
        double averageRate = rates.stream()
                .mapToDouble(Rate::getScore)
                .average()
                .orElse(0.0);

        // 更新书籍信息
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("书籍不存在"));
        product.setRate(averageRate);
        productRepository.save(product);
    }
}