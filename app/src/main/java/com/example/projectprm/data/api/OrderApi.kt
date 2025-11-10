package com.example.projectprm.data.api

import com.example.projectprm.data.api.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    @GET("api/orders")
    suspend fun getMyOrders(): Response<List<OrderDto>>
    
    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Int): Response<OrderDto>
    
    @POST("api/orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<ApiResponse<OrderDto>>
}

data class OrderDto(
    val id: Int,
    val orderDate: String,
    val status: String,
    val totalAmount: Double,
    val shippingAddress: String,
    val items: List<OrderItemDto>
)

data class OrderItemDto(
    val id: Int,
    val bookTitle: String,
    val quantity: Int,
    val price: Double
)

data class CreateOrderRequest(
    val shippingAddress: String,
    val notes: String?
)
