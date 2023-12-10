package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ItemRestController {
    private ItemService itemService;

    @GetMapping("/getItemCategory")
    public ResponseEntity<List<ItemDto>> getItemCategory(String category){
        List<ItemDto> resultDTOList = itemService.findByCategory(category);
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }


}
