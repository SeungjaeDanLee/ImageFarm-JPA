package com.shop.service;

import com.shop.entity.Item;
import com.shop.entity.LikeEntity;
import com.shop.entity.Member;
import com.shop.repository.ItemRepository;
import com.shop.repository.LikeRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
//@Transactional
//@RequiredArgsConstructor
//public class LikeService {
//    private final ItemRepository itemRepository;
//    private final MemberRepository memberRepository;
//
//
//}


@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final ItemRepository itemRepository;

    public boolean addLike(Member member, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        //중복 좋아요 방지
        if(isNotAlreadyLike(member, item)) {
            likeRepository.save(new LikeEntity(item, member));
            return true;
        } else {
            likeRepository.deleteLike(itemId, member.getId());
        }
        return false;
    }
    //사용자가 이미 좋아요 한 게시물인지 체크
    public boolean isNotAlreadyLike(Member member, Item item) {
        return likeRepository.findByMemberAndItem(member, item).isEmpty();
    }
}