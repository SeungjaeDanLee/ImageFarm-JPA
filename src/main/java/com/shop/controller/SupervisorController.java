package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.*;
import com.shop.repository.*;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/supervisor")
@Controller
@RequiredArgsConstructor
public class SupervisorController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;


    @GetMapping("/page")
    public String supervisorPage() {
        // 관리자 페이지의 뷰 이름을 반환합니다.
        return "/supervisor/supervisorPage";
    }

    @GetMapping(value = {"/items", "/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "/supervisor/svItemMng";
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        try {
            // 1. 상품과 관련된 주문 항목을 삭제합니다.
            List<OrderItem> orderItems = orderItemRepository.findByItemId(itemId);
            orderItemRepository.deleteAll(orderItems);

            // 2. 상품과 관련된 cart_item 레코드를 삭제합니다.
            List<CartItem> cartItems = cartItemRepository.findByItemId(itemId);
            cartItemRepository.deleteAll(cartItems);

            // 3. 상품을 삭제합니다.
            itemService.deleteItem(itemId);

            return new ResponseEntity<String>("상품이 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("상품 삭제 중 오류가 발생했습니다.");
        }
    }



    @GetMapping("/members")
    public String listMembers(Model model,
                              @RequestParam(defaultValue = "") String searchType,
                              @RequestParam(defaultValue = "") String keyword,
                              @PageableDefault(size = 5, sort = "id",
                                      direction = Sort.Direction.DESC) Pageable pageable) {

        if ("name".equals(searchType)) {
            model.addAttribute("members",
                    memberService.getMembersByName(keyword, pageable));
        } else if ("email".equals(searchType)) {
            model.addAttribute("members",
                    memberService.getMembersByEmail(keyword, pageable));
        } else {
            model.addAttribute("members",
                    memberService.getAllMembers(pageable));
        }

        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "/supervisor/svMembersMng"; // 렌더링할 Thymeleaf 템플릿 경로
    }



    @DeleteMapping("/members/{memberId}")
    @Transactional
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        try {
            // 1. 회원과 관련된 주문을 삭제합니다.
            List<Order> orders = orderRepository.findByMemberId(memberId);

            // 1-1. 주문과 관련된 주문 항목(orderItem)을 먼저 삭제합니다.
            for (Order order : orders) {
                List<OrderItem> orderItems = orderItemRepository.getByOrderId(order.getId());
                if(orderItems.size() > 0) {
                    orderItemRepository.deleteOrderItems(order.getId());
                }
            }
            // 1-2. 주문을 삭제합니다.
            orderRepository.deleteOrder(memberId);
            // 2. 회원과 관련된 카트 아이템을 삭제합니다.
            List<Cart> carts = cartRepository.getByMemberId(memberId);
            for (Cart cart : carts) {
                List<CartItem> cartItems = cartItemRepository.getByCartId(cart.getId());
                if(cartItems.size() > 0) {
                    cartItemRepository.deleteCartItems(cart.getId());
                }
            }
            // 3. 회원과 관련된 카트를 삭제합니다.
            cartRepository.deleteCart(memberId);


            List<Item> itemsList = itemRepository.getByCreatedBy(memberRepository.findById(memberId).get().getEmail());
            for (Item item : itemsList) {
                List<ItemImg> itemImgs = itemImgRepository.getByItemId(item.getId());
                if(itemImgs.size() > 0) {
                    itemImgRepository.deleteItemImgs(item.getId());
                }
            }

            for (Item item : itemsList) {
                if(itemsList.size() > 0) {
                    itemRepository.deleteById(item.getId());
                }
            }
//            itemRepository.allDeleteItems(memberRepository.findById(memberId).get().getEmail());

            // 4. 회원을 삭제합니다.
            memberRepository.deleteById(memberId);

            return new ResponseEntity<String>("사용자 아이디가 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("아이디 삭제 중 오류가 발생했습니다.");
        }
    }


    @GetMapping("/orders")
    public String listOrders(Model model,
                             @RequestParam(defaultValue = "") String searchType,
                             @RequestParam(defaultValue = "") String keyword,
                             @PageableDefault(size = 5, sort = "id",
                                     direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Order> orders = orderService.getAllOrders(pageable); // orderService를 통해 구매 목록을 가져옵니다.

        model.addAttribute("orders", orders); // 구매 목록을 모델에 추가합니다.

        return "/supervisor/svOrdersMng"; // 렌더링할 Thymeleaf 템플릿 경로
    }
}
