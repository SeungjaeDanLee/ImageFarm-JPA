package com.shop.controller;

import com.shop.entity.ItemImg;
import com.shop.service.ItemImgService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Log4j2
public class ImageController {

    @Autowired
    private ItemImgService itemImgService;

    @GetMapping("/download/{imgId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long imgId){
        ItemImg itemImg = itemImgService.getItemImgById(imgId);
        if(itemImg != null){
            byte[] fileData = itemImg.getFileData(itemImg.getImgName());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + itemImg.getImgName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileData.length)
                    .body(fileData);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
