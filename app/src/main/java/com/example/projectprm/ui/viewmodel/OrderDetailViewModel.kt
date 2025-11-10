package com.example.projectprm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectprm.data.api.dto.OrderDto
import com.example.projectprm.data.repository.OrderRepository
import com.example.projectprm.data.util.Resource
import com.example.projectprm.util.orderApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for OrderDetailScreen
 * Manages individual order details and cancellation
 */
class OrderDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val orderRepository = OrderRepository(application.orderApi())
    
    // Order details state
    private val _orderState = MutableStateFlow<Resource<OrderDto>>(Resource.Loading())
    val orderState: StateFlow<Resource<OrderDto>> = _orderState.asStateFlow()
    
    // Cancel order state
    private val _cancelOrderState = MutableStateFlow<Resource<OrderDto>?>(null)
    val cancelOrderState: StateFlow<Resource<OrderDto>?> = _cancelOrderState.asStateFlow()
    
    /**
     * Load order details by ID
     * @param orderId ID of the order to load
     */
    fun loadOrder(orderId: Int) {
        viewModelScope.launch {
            orderRepository.getOrderById(orderId).collect { resource ->
                _orderState.value = resource
            }
        }
    }
    
    /**
     * Cancel an order
     * @param orderId ID of the order to cancel
     */
    fun cancelOrder(orderId: Int) {
        viewModelScope.launch {
            orderRepository.cancelOrder(orderId).collect { resource ->
                _cancelOrderState.value = resource
                // Reload order after successful cancellation
                if (resource is Resource.Success) {
                    loadOrder(orderId)
                }
            }
        }
    }
    
    /**
     * Reset cancel order state
     */
    fun resetCancelOrderState() {
        _cancelOrderState.value = null
    }
    
    /**
     * Check if order can be cancelled
     * @return true if order status allows cancellation
     */
    fun canCancelOrder(): Boolean {
        return when (val state = _orderState.value) {
            is Resource.Success -> {
                val order = state.data
                order?.status in listOf("PENDING", "CONFIRMED")
            }
            else -> false
        }
    }
}
