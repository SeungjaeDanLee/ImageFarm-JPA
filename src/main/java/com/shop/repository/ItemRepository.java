    package com.shop.repository;

    import com.shop.dto.ItemDto;
    import com.shop.dto.ItemJoinInterface;
    import com.shop.dto.ItemSearchDto;
    import com.shop.dto.MainItemDto;
    import com.shop.entity.Cart;
    import com.shop.entity.Item;
    import com.shop.service.ItemService;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;

    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.querydsl.QuerydslPredicateExecutor;
    import org.springframework.data.repository.query.Param;

    import java.util.List;

    import javax.transaction.Transactional;

    public interface ItemRepository extends JpaRepository<Item, Long>,
            QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

        List<Item> findByItemNm(String itemNm);

        List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

        List<Item> findByPriceLessThan(Integer price);

        List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);


        List<ItemDto> findByCategory(String category);

        List<Item> findByHashtag(String hashtag);

        List<Item> findByHashtagOrItemNm(String itemNm,String hashtag);

        @Query("select i from Item i where i.itemDetail like " +
                "%:itemDetail% order by i.price desc")
        List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


        @Query(value="select * from item i where i.hashtag like " +
                "%:hashtag% order by i.hashtag desc", nativeQuery = true)
        List<Item> findByHashtagByNative(@Param("hashtag") String hashtag);

        Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable,String hashtag);


        @Modifying
        @Transactional
        @Query("DELETE FROM ItemImg WHERE item.id = :itemId")
        void deleteItemImagesByItemId(Long itemId);


//        Page<Item> findByCreatedByAndOtherCriteria(String loggedInUsername, ItemSearchDto itemSearchDto, Pageable pageable);


        @Modifying
        @Query("update Item i set i.view = i.view + 1 where i.id = :id")
        int updateView(Long id);

//        @Query("SELECT i FROM Item i ORDER BY i.view DESC")
//        Page<Item> findAllOrderByViewDesc(Pageable pageable);

        @Modifying
        @Query("update Item i set i.heart = i.heart+1 where i.id = :itemId")
        int updateHeartPlus(Long itemId);

        @Modifying
        @Query("update Item i set i.heart = i.heart-1  where i.id = :itemId")
        int updateHeartMinus(Long itemId);
//        @Modifying
//        @Query("update Item i set i.heartCount = :heartCount  where i.id = :itemId")
//        int updateHeartCount(Long itemId,int heartCount);

        @Query("select i from Item i where i.createdBy = :createdBy")
        List<Item> getByCreatedBy(String createdBy);

        @Modifying
        @Query("delete from Item i where i.createdBy = :createdBy")
        void allDeleteItems(String createdBy);


        @Query(value = "SELECT m.img_url, i.item_id, i.item_nm, i.item_detail, i.price " +
                       "  FROM item i inner JOIN Item_Img m on m.item_id = i.item_id " +
                        "where i.category = 'ILLUST'"+
                       " ORDER BY i.view DESC limit 4",nativeQuery = true)
        List<ItemJoinInterface> getByILLUSTView();

        @Query(value = "SELECT m.img_url, i.item_id, i.item_nm, i.item_detail, i.price " +
                "  FROM item i inner JOIN Item_Img m on m.item_id = i.item_id " +
                "where i.category = 'ICON'"+
                " ORDER BY i.view DESC limit 4",nativeQuery = true)
        List<ItemJoinInterface> getByICONView();

        @Query(value = "SELECT m.img_url, i.item_id, i.item_nm, i.item_detail, i.price " +
                "  FROM item i inner JOIN Item_Img m on m.item_id = i.item_id " +
                "where i.category = 'PHOTO'"+
                " ORDER BY i.view DESC limit 4",nativeQuery = true)
        List<ItemJoinInterface> getByPHOTOView();
    }
