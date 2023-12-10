package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String hashtag;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String hashtag, String imgUrl,Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.hashtag = hashtag;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}