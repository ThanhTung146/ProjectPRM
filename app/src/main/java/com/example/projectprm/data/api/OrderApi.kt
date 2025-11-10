package com.example.projectprm.data.api

import com.example.projectprm.data.api.dto.ApiResponse
import com.example.projectprm.data.api.dto.CreateOrderRequest
import com.example.projectprm.data.api.dto.OrderDto
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    @GET("api/orders")
    suspend fun getOrders(): Response<List<OrderDto>>
    
    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Int): Response<OrderDto>
    
    @POST("api/orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<OrderDto>
    
    @PUT("api/orders/{id}/cancel")
    suspend fun cancelOrder(@Path("id") id: Int): Response<OrderDto>
}
