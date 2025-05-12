package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateVO {
    private Integer id; // 帖子 ID
    private String title; // 帖子标题
    private String content; // 帖子正文
    private String image; // 图片 URL（可选）
    private Float rate; // 书籍评分（可选，仅适用于 BookComment 分类）
}