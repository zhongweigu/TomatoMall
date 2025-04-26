package com.example.tomatomall.vo;

import lombok.Data;

@Data
public class AdvertisementVO {
    private Integer id;
    private String title;
    private String content;
    private String imgUrl;
    private Integer productId; // 确保返回正确的 productId
}