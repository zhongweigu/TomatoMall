package com.example.tomatomall.po;

import com.example.tomatomall.vo.AdvertisementVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_advertisement_product"))
    private Product product;

    public AdvertisementVO toVO(){
        AdvertisementVO vo = new AdvertisementVO();
        vo.setTitle(title);
        vo.setContent(content);
        vo.setImgUrl(imgUrl);
        vo.setId(id);
        vo.setProductId(product.getId());
        return vo;
    }
}