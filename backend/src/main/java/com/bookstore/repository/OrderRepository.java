package com.bookstore.repository;

import com.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findByUserUserIdOrderByOrderDateDesc(Integer userId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByUserUserIdAndStatus(Integer userId, Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId ORDER BY o.orderDate DESC")
    List<Order> findUserOrders(Integer userId);
    
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllOrdersOrderByDateDesc();
}
