package com.example.tomatomall.po;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_content_product"))
    @JsonBackReference
    @NotNull
    private Product product;

    @Basic
    @NotNull
    private String contentUrl;
}
