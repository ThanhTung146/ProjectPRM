package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Review;
import com.bookstore.entity.User;
import com.bookstore.repository.ReviewRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    public List<Review> getBookReviews(Integer bookId) {
        return reviewRepository.findByBookBookIdOrderByCreatedAtDesc(bookId);
    }

    public Double getAverageRating(Integer bookId) {
        Double avg = reviewRepository.getAverageRatingByBookId(bookId);
        return avg != null ? avg : 0.0;
    }

    public Long getReviewCount(Integer bookId) {
        return reviewRepository.getReviewCountByBookId(bookId);
    }

    @Transactional
    public Review createReview(Integer userId, Integer bookId, Integer rating, String comment) {
        if (reviewRepository.existsByUserUserIdAndBookBookId(userId, bookId)) {
            throw new RuntimeException("You have already reviewed this book");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Book book = bookService.getBookById(bookId);

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Integer reviewId, Integer rating, String comment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
