package com.example.tomatomall.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    private String image;

    private Integer likesCount;

    @Column(name = "comments_number")
    private Integer commentsNumber;

    @Column(name = "time_stamp")
    private Date timeStamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    private String book;

    private Double bookRate;
}