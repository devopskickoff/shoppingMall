package com.shop.wear.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter @Setter
public class ItemImg extends BaseEntity{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName;

    private String oriTmgName;

    private String imgUrl;

    private String repimgYn;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriTmgName, String imgName, String imgUrl){
        this.oriTmgName = oriTmgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
