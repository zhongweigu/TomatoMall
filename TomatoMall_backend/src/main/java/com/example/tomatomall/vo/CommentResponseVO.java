package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseVO {
    private Integer id;
    private String content;
    private String image;
    private Date timeStamp;
    private String nickname;
    private String avatar;
}