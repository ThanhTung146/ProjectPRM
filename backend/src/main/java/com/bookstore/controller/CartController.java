package com.bookstore.controller;

import com.bookstore.dto.AddToCartRequest;
import com.bookstore.dto.ApiResponse;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.User;
import com.bookstore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.bookstore.repository.UserRepository;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItem>>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<CartItem> cartItems = cartService.getUserCart(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Cart retrieved successfully", cartItems));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            CartItem cartItem = cartService.addToCart(user.getUserId(), 
                    request.getBookId(), request.getQuantity());
            return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(
            @PathVariable Integer cartItemId,
            @RequestParam Integer quantity) {
        try {
            CartItem cartItem = cartService.updateCartItemQuantity(cartItemId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Cart updated", cartItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Integer cartItemId) {
        try {
            cartService.removeFromCart(cartItemId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartService.clearCart(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Cart cleared"));
    }
}
