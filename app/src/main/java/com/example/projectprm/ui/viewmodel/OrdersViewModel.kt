package com.example.projectprm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectprm.data.api.dto.OrderDto
import com.example.projectprm.data.repository.OrderRepository
import com.example.projectprm.util.Resource
import com.example.projectprm.util.orderApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for OrdersScreen
 * Manages order list state and filtering
 */
class OrdersViewModel(application: Application) : AndroidViewModel(application) {
    
    private val orderRepository = OrderRepository(application.orderApi())
    
    // Orders list state
    private val _ordersState = MutableStateFlow<Resource<List<OrderDto>>>(Resource.Loading())
    val ordersState: StateFlow<Resource<List<OrderDto>>> = _ordersState.asStateFlow()
    
    // Selected filter
    private val _selectedFilter = MutableStateFlow<String?>(null)
    val selectedFilter: StateFlow<String?> = _selectedFilter.asStateFlow()
    
    init {
        loadOrders()
    }
    
    /**
     * Load all orders
     */
    fun loadOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collect { resource ->
                _ordersState.value = resource
            }
        }
    }
    
    /**
     * Filter orders by status
     * @param status Order status to filter by (null = show all)
     */
    fun filterByStatus(status: String?) {
        _selectedFilter.value = status
    }
    
    /**
     * Get filtered orders based on selected status
     * @return Filtered list of orders
     */
    fun getFilteredOrders(): List<OrderDto>? {
        return when (val state = _ordersState.value) {
            is Resource.Success -> {
                val allOrders = state.data ?: emptyList()
                val filter = _selectedFilter.value
                
                if (filter == null) {
                    allOrders
                } else {
                    allOrders.filter { it.status == filter }
                }
            }
            else -> null
        }
    }
    
    /**
     * Refresh orders list
     */
    fun refresh() {
        loadOrders()
    }
}
