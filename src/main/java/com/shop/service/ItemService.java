package com.shop.service;

import com.shop.dto.*;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.LikeEntity;
import com.shop.entity.Member;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    private final OrderRepository orderRepository;


    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            if(itemImgFileList.get(i).getOriginalFilename() != null && !itemImgFileList.get(i).getOriginalFilename().isEmpty()){
                ItemImg itemImg = new ItemImg();
                itemImg.setItem(item);

                if(i == 0)
                    itemImg.setRepimgYn("Y");
                else
                    itemImg.setRepimgYn("N");

                itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
            }

        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable, String category){
        return itemRepository.getMainItemPage(itemSearchDto, pageable, category);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPageOr(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPageOr(itemSearchDto, pageable);
    }


    public List<ItemDto> findByCategory(String category){
        return  itemRepository.findByCategory(category);
    }

    public List<ItemJoinInterface> getByILLUSTView(){
        return  itemRepository.getByILLUSTView();
    }
    public List<ItemJoinInterface> getByICONView(){
        return  itemRepository.getByICONView();
    }
    public List<ItemJoinInterface> getByPHOTOView(){
        return  itemRepository.getByPHOTOView();
    }


    public void deleteItem(Long itemId) throws Exception {
        // 상품을 데이터베이스에서 조회합니다.
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        // 만약 상품이 존재한다면 삭제합니다.
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();

            // 관련된 이미지들도 함께 삭제합니다. (이 부분은 itemRepository에 정의된 메서드를 활용하거나 직접 구현해야 합니다)
            itemImgService.deleteItemImagesByItemId(itemId);

            // 상품을 삭제합니다.
            itemRepository.delete(item);
        } else {
            throw new EntityNotFoundException("상품을 찾을 수 없습니다.");
        }
    }

    @Transactional
    public int updateView(Long id) {
        return itemRepository.updateView(id);
    }

    @Transactional
    public int updateHeart(long itemId,int heart){
        int result = 0;

        if(heart==0){
            result = itemRepository.updateHeartMinus(itemId);
        }else if(heart==1){
            result =itemRepository.updateHeartPlus(itemId);
        }

        return result;
    }

//    @Transactional
//    public int updateHeartCount(long itemId,int heartCount){
//        return itemRepository.updateHeartCount(itemId,heartCount);
//    }





}
