package com.example.projectprm.data.api.dto

import com.google.gson.annotations.SerializedName

/**
 * Request DTO for creating an order
 */
data class CreateOrderRequest(
    @SerializedName("shippingAddress")
    val shippingAddress: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    @SerializedName("notes")
    val notes: String? = null
)

/**
 * Response DTO for order
 */
data class OrderDto(
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("orderDate")
    val orderDate: Long, // Timestamp in milliseconds
    @SerializedName("totalAmount")
    val totalAmount: Double,
    @SerializedName("status")
    val status: String, // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    @SerializedName("paymentMethod")
    val paymentMethod: String, // COD, VNPAY, MOMO
    @SerializedName("paymentStatus")
    val paymentStatus: String, // UNPAID, PAID, REFUNDED
    @SerializedName("shippingAddress")
    val shippingAddress: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("orderItems")
    val orderItems: List<OrderItemDto>
)

/**
 * Response DTO for order item
 */
data class OrderItemDto(
    @SerializedName("orderItemId")
    val orderItemId: Int,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("book")
    val book: Book,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("priceAtPurchase")
    val priceAtPurchase: Double,
    @SerializedName("subtotal")
    val subtotal: Double
)
