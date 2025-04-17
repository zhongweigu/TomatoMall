package com.example.tomatomall.service;

import com.example.tomatomall.vo.AdvertisementVO;

import java.util.List;

public interface AdvertisementService {
    List<AdvertisementVO> getAllAdvertisements();

    AdvertisementVO createAdvertisement(String title, String content, String imageUrl, Integer productId);

    AdvertisementVO updateAdvertisement(Integer id, String title, String content, String imageUrl, Integer productId);

    boolean deleteAdvertisement(Integer id);
}