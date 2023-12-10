package com.shop.controller;

import com.shop.dto.ItemJoinInterface;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ItemService itemService;

    @GetMapping(value = "/index")
    public String index(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 50);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable,"");
        List<ItemJoinInterface> viewicon = itemService.getByICONView();
        List<ItemJoinInterface> viewIllust = itemService.getByILLUSTView();
        List<ItemJoinInterface> viewPhoto = itemService.getByPHOTOView();



        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("viewicon",viewicon);
        model.addAttribute("viewIllust",viewIllust);
        model.addAttribute("viewPhoto",viewPhoto);



        return "index";
    }


    @GetMapping(value = "/icon/{category}")
    public String icon(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, @PathVariable("category") String category){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable, category);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "/category/index_icon";
    }

    @GetMapping(value = "/illust/{category}")
    public String illust(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model,  @PathVariable("category") String category){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable, category);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "/category/index_illust";
    }

    @GetMapping(value = "/photo/{category}")
    public String photo(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, @PathVariable("category") String category){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable, category);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "/category/index_photo";
    }


//    @GetMapping("/popular-items")
//    public String getPopularItems(Model model) {
//        // 조회수가 높은 순으로 첫 번째 페이지의 아이템 목록을 가져옴
//        PageRequest pageRequest = PageRequest.of(0, 4);
//        Page<Item> items = itemRepository.findAllOrderByViewDesc(pageRequest);
//
//        model.addAttribute("items", items);
//
//        return "items";


//    @GetMapping("/index")
//    public String getPopularItems(Model model) {
//        // 조회수가 높은 순으로 첫 번째 페이지의 아이템 목록을 가져옴
//        PageRequest pageRequest = PageRequest.of(0, 4);
//        Page<Item> PopularItems = itemRepository.findAllOrderByViewDesc(pageRequest);
//
//        model.addAttribute("getPopularItems", PopularItems);
//
//        return "index";
//
//
//    }

}