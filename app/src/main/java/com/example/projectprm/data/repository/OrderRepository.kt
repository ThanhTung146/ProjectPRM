package com.example.projectprm.data.repository

import com.example.projectprm.data.api.OrderApi
import com.example.projectprm.data.api.dto.CreateOrderRequest
import com.example.projectprm.data.api.dto.OrderDto
import com.example.projectprm.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository for order-related operations
 * Handles all order management with the backend API
 */
class OrderRepository(
    private val orderApi: OrderApi
) {
    
    /**
     * Get all orders for the current user
     * @return Flow of Resource containing list of orders
     */
    fun getAllOrders(): Flow<Resource<List<OrderDto>>> = flow {
        try {
            emit(Resource.Loading())
            val response = orderApi.getOrders()
            
            if (response.isSuccessful) {
                response.body()?.let { orders ->
                    emit(Resource.Success(orders))
                } ?: emit(Resource.Error("Empty response from server"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to load orders"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    /**
     * Get order details by ID
     * @param orderId ID of the order to retrieve
     * @return Flow of Resource containing order details
     */
    fun getOrderById(orderId: Int): Flow<Resource<OrderDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = orderApi.getOrderById(orderId)
            
            if (response.isSuccessful) {
                response.body()?.let { order ->
                    emit(Resource.Success(order))
                } ?: emit(Resource.Error("Empty response from server"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to load order"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    /**
     * Create a new order from cart items
     * @param shippingAddress Delivery address
     * @param phoneNumber Contact phone number
     * @param paymentMethod Payment method (COD, VNPAY, MOMO)
     * @param notes Optional order notes
     * @return Flow of Resource containing created order
     */
    fun createOrder(
        shippingAddress: String,
        phoneNumber: String,
        paymentMethod: String,
        notes: String? = null
    ): Flow<Resource<OrderDto>> = flow {
        try {
            emit(Resource.Loading())
            val request = CreateOrderRequest(
                shippingAddress = shippingAddress,
                phoneNumber = phoneNumber,
                paymentMethod = paymentMethod,
                notes = notes
            )
            val response = orderApi.createOrder(request)
            
            if (response.isSuccessful) {
                response.body()?.let { order ->
                    emit(Resource.Success(order))
                } ?: emit(Resource.Error("Empty response from server"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to create order"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    /**
     * Cancel an order
     * @param orderId ID of the order to cancel
     * @return Flow of Resource containing updated order
     */
    fun cancelOrder(orderId: Int): Flow<Resource<OrderDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = orderApi.cancelOrder(orderId)
            
            if (response.isSuccessful) {
                response.body()?.let { order ->
                    emit(Resource.Success(order))
                } ?: emit(Resource.Error("Empty response from server"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to cancel order"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
