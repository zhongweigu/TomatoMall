package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseVO {
    private Integer id;
    private String category;
    private String title;
    private String briefContent;
    private String image;
    private Integer commentsNumber;
    private Date timeStamp;
    private String nickname;
    private String avatar;
    private String bookId;
    private Double rating;
    private Integer likes;
    private Integer userId;
}