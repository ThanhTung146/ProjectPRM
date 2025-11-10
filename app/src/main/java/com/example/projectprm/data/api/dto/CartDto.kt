package com.example.projectprm.data.api.dto

import com.example.projectprm.data.model.Book
import com.google.gson.annotations.SerializedName

/**
 * Request DTO for adding item to cart
 */
data class AddToCartRequest(
    @SerializedName("bookId")
    val bookId: Int,
    @SerializedName("quantity")
    val quantity: Int
)

/**
 * Request DTO for updating cart item quantity
 */
data class UpdateCartItemRequest(
    @SerializedName("quantity")
    val quantity: Int
)

/**
 * Response DTO for cart item
 */
data class CartItemDto(
    @SerializedName("cartItemId")
    val cartItemId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("book")
    val book: Book,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("addedAt")
    val addedAt: String
)
