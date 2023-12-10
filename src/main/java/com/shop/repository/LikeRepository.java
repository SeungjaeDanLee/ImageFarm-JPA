package com.shop.repository;

import com.shop.entity.Item;
import com.shop.entity.LikeEntity;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByMemberAndItem(Member member, Item item);


    @Modifying
    @Query("delete from LikeEntity l where l.item.id = :itemId and l.member.id = :memberId")
    void deleteLike(Long itemId, Long memberId);
}