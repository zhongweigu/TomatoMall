package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateVO {
    private String category;
    private String title;
    private String content;
    private String image;
    private Integer userId;
    private Integer bookId;
    private Float rating;
}