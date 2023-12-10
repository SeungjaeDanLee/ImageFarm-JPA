package com.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.constant.ItemCategory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"item"})
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;       //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Enumerated(EnumType.STRING)
    private ItemCategory category;    //카테고리

    @Column(name="hashtag")
    private String hashtag;

    @Column(name="price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int heart;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    Set<LikeEntity> likes = new HashSet<>();




    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
//        String str = "";
//        for (int i = 0;i<itemFormDto.getHashtagList().size();i++){
//            if(i == itemFormDto.getHashtagList().size()-1){
//                str += itemFormDto.getHashtagList().get(i);
//            }else if (i < itemFormDto.getHashtagList().size()){
//                str += itemFormDto.getHashtagList().get(i) + ",";
//            }
//        }
        this.hashtag = itemFormDto.getHashtag();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }



}