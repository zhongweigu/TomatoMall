package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeRequestVO {
    private Integer userId;
    private Integer postId;
    private Integer commentId;
}
