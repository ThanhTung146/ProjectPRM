package com.example.projectprm.data.api

import com.example.projectprm.data.api.dto.AddToCartRequest
import com.example.projectprm.data.api.dto.ApiResponse
import com.example.projectprm.data.api.dto.CartItemDto
import com.example.projectprm.data.api.dto.UpdateCartItemRequest
import retrofit2.Response
import retrofit2.http.*

interface CartApi {
    @GET("api/cart")
    suspend fun getCart(): Response<List<CartItemDto>>
    
    @POST("api/cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<ApiResponse<CartItemDto>>
    
    @PUT("api/cart/{id}")
    suspend fun updateCartItem(
        @Path("id") id: Int,
        @Body request: UpdateCartItemRequest
    ): Response<CartItemDto>
    
    @DELETE("api/cart/{id}")
    suspend fun deleteCartItem(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    @DELETE("api/cart")
    suspend fun clearCart(): Response<ApiResponse<Unit>>
}
