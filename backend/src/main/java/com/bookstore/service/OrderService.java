package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.findByUserUserIdOrderByOrderDateDesc(userId);
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrdersOrderByDateDesc();
    }

    @Transactional
    public Order createOrder(Integer userId, String shippingAddress, String phoneNumber, 
                            String paymentMethod, String notes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartService.getUserCart(userId);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentMethod(Order.PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        order.setPaymentStatus(Order.PaymentStatus.UNPAID);
        order.setShippingAddress(shippingAddress);
        order.setPhoneNumber(phoneNumber);
        order.setNotes(notes);

        Order savedOrder = orderRepository.save(order);

        // Create order items and update stock
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getBook().getPrice());
            orderItem.setSubtotal(cartItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            
            savedOrder.getOrderItems().add(orderItem);

            // Update stock
            bookService.updateStock(cartItem.getBook().getBookId(), cartItem.getQuantity());
        }

        // Clear cart
        cartService.clearCart(userId);

        return orderRepository.save(savedOrder);
    }

    @Transactional
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        return orderRepository.save(order);
    }

    @Transactional
    public Order updatePaymentStatus(Integer orderId, String paymentStatus) {
        Order order = getOrderById(orderId);
        order.setPaymentStatus(Order.PaymentStatus.valueOf(paymentStatus.toUpperCase()));
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Integer orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != Order.OrderStatus.PENDING && 
            order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel order in current status");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Restore stock
        for (OrderItem item : order.getOrderItems()) {
            Book book = item.getBook();
            book.setStockQuantity(book.getStockQuantity() + item.getQuantity());
        }
    }
}
