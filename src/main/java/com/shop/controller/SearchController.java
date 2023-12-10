package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final ItemService itemService;
    @GetMapping("/search")
    public String search(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 21);
        Page<MainItemDto> items;
        if(itemSearchDto.getSearchBy()==null || itemSearchDto.getSearchBy().isEmpty()){
            items = itemService.getMainItemPageOr(itemSearchDto, pageable);
        }else{
            items = itemService.getMainItemPage(itemSearchDto, pageable, "");
        }


        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "/item/itemSearch";
    }

}
