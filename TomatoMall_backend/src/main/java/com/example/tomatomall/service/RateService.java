package com.example.tomatomall.service;

import com.example.tomatomall.vo.RateCreateVO;

public interface RateService {
    String addRate(RateCreateVO rateCreateVO);
    void deleteRate(Integer Id);
    void deleteRatesByPostId(Integer postId);

    String getRate(Integer postId);
}