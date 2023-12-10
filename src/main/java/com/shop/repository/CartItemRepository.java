package com.shop.repository;

import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.dto.CartDetailDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
            )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

    List<CartItem> findByCart(Cart cart);


    List<CartItem> findByCartMemberId(Long memberId);

    List<CartItem> findByItemId(Long itemId);

    @Query("select c from CartItem c where c.cart.id = :id")
    List<CartItem> getByCartId(Long id);

    @Modifying
    @Query("delete from CartItem c where c.cart.id = :cartId")
    void deleteCartItems(Long cartId);
}