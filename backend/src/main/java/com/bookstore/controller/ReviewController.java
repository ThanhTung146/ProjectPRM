package com.bookstore.controller;

import com.bookstore.dto.ApiResponse;
import com.bookstore.dto.CreateReviewRequest;
import com.bookstore.entity.Review;
import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<Review>>> getBookReviews(@PathVariable Integer bookId) {
        List<Review> reviews = reviewService.getBookReviews(bookId);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }

    @GetMapping("/book/{bookId}/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBookReviewStats(@PathVariable Integer bookId) {
        Double avgRating = reviewService.getAverageRating(bookId);
        Long count = reviewService.getReviewCount(bookId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", avgRating);
        stats.put("totalReviews", count);
        
        return ResponseEntity.ok(ApiResponse.success("Review stats retrieved", stats));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Review review = reviewService.createReview(
                    user.getUserId(),
                    request.getBookId(),
                    request.getRating(),
                    request.getComment()
            );
            return ResponseEntity.ok(ApiResponse.success("Review created successfully", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Review>> updateReview(
            @PathVariable Integer id,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {
        try {
            Review review = reviewService.updateReview(id, rating, comment);
            return ResponseEntity.ok(ApiResponse.success("Review updated successfully", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Integer id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(ApiResponse.success("Review deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
