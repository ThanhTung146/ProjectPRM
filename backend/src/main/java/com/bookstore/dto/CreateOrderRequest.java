package com.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
    
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotNull(message = "Payment method is required")
    private String paymentMethod; // COD, VNPAY, MOMO
    
    private String notes;
}
