package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Entity
@Table(name="item_img")
@Getter @Setter
@Log4j2
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }


    public byte[] getFileData(String imgName) {

//        File imageFile = new File("file:///C:/shop/item/"  + imgName);
        File imageFile = new File("C:/shop/item/"  + imgName);

        try{
            return Files.readAllBytes((imageFile.toPath()));
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}