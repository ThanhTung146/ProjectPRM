package com.example.projectprm.data.api

import com.example.projectprm.data.api.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface CartApi {
    @GET("api/cart")
    suspend fun getCartItems(): Response<List<CartItemDto>>
    
    @POST("api/cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<ApiResponse<CartItemDto>>
    
    @PUT("api/cart/{id}")
    suspend fun updateCartItem(
        @Path("id") id: Int,
        @Body request: UpdateCartRequest
    ): Response<ApiResponse<CartItemDto>>
    
    @DELETE("api/cart/{id}")
    suspend fun removeFromCart(@Path("id") id: Int): Response<ApiResponse<Unit>>
}

data class CartItemDto(
    val id: Int,
    val book: BookDto,
    val quantity: Int
)

data class BookDto(
    val id: Int,
    val title: String,
    val author: String,
    val price: Double,
    val coverImage: String?
)

data class AddToCartRequest(
    val bookId: Int,
    val quantity: Int
)

data class UpdateCartRequest(
    val quantity: Int
)
