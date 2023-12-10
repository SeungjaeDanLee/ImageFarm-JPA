package com.shop.repository;

import com.shop.entity.Cart;
import com.shop.entity.Member;
import com.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);

    Cart findByMember(Member member);

    @Query("select c from Cart c where c.member.id = :memberId")
    List<Cart> getByMemberId(Long memberId);

    @Modifying
    @Query("delete from Cart c where c.member.id = :memberId")
    void deleteCart(Long memberId);
}