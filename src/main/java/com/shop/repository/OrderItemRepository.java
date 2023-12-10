package com.shop.repository;

import com.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByItemId(Long itemId);

    @Query("select o from OrderItem o where o.order.id = :id")
    List<OrderItem> getByOrderId(Long id);


    @Modifying
    @Query("delete from OrderItem o where o.order.id = :id")
    void deleteOrderItems(Long id);
}