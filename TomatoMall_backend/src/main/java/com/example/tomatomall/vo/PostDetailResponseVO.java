package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostDetailResponseVO {
    private Integer id;
    private String category;
    private String title;
    private String content;
    private String image;
    private Integer commentsNumber;
    private Date timeStamp;
    private Integer userId;
    private Integer bookId;
}