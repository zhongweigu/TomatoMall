package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RateCreateVO {
    private Integer userId; // 用户 ID
    private Integer productId; // 书籍 ID
    private Integer postId; // 帖子 ID
    private Double score; // 评分（1~10）
}