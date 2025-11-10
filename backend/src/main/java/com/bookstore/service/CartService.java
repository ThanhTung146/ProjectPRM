package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.User;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    public List<CartItem> getUserCart(Integer userId) {
        return cartItemRepository.findByUserUserId(userId);
    }

    @Transactional
    public CartItem addToCart(Integer userId, Integer bookId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Book book = bookService.getBookById(bookId);

        if (book.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem existingCartItem = cartItemRepository
                .findByUserUserIdAndBookBookId(userId, bookId)
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            return cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public CartItem updateCartItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (cartItem.getBook().getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Integer userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
