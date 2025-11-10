package com.bookstore.repository;

import com.bookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
    List<OrderItem> findByOrderOrderId(Integer orderId);
    
    List<OrderItem> findByBookBookId(Integer bookId);
}
