package com.bookstore.repository;

import com.bookstore.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    
    List<Review> findByBookBookIdOrderByCreatedAtDesc(Integer bookId);
    
    List<Review> findByUserUserId(Integer userId);
    
    Optional<Review> findByUserUserIdAndBookBookId(Integer userId, Integer bookId);
    
    Boolean existsByUserUserIdAndBookBookId(Integer userId, Integer bookId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.bookId = :bookId")
    Double getAverageRatingByBookId(Integer bookId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.book.bookId = :bookId")
    Long getReviewCountByBookId(Integer bookId);
}
