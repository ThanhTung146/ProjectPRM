package com.bookstore.repository;

import com.bookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
    List<CartItem> findByUserUserId(Integer userId);
    
    Optional<CartItem> findByUserUserIdAndBookBookId(Integer userId, Integer bookId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.user.userId = :userId")
    void deleteByUserId(Integer userId);
    
    @Modifying
    @Transactional
    void deleteByUserUserIdAndBookBookId(Integer userId, Integer bookId);
}
