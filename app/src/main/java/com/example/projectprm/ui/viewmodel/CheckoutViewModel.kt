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
 * ViewModel for CheckoutScreen
 * Manages order creation and checkout process
 */
class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    
    private val orderRepository = OrderRepository(application.orderApi())
    
    // Create order state
    private val _createOrderState = MutableStateFlow<Resource<OrderDto>?>(null)
    val createOrderState: StateFlow<Resource<OrderDto>?> = _createOrderState.asStateFlow()
    
    /**
     * Place an order with cart items
     * @param shippingAddress Delivery address
     * @param phoneNumber Contact phone number
     * @param paymentMethod Payment method (COD, VNPAY, MOMO)
     * @param notes Optional order notes
     */
    fun placeOrder(
        shippingAddress: String,
        phoneNumber: String,
        paymentMethod: String,
        notes: String? = null
    ) {
        viewModelScope.launch {
            orderRepository.createOrder(
                shippingAddress = shippingAddress,
                phoneNumber = phoneNumber,
                paymentMethod = paymentMethod,
                notes = notes
            ).collect { resource ->
                _createOrderState.value = resource
            }
        }
    }
    
    /**
     * Reset create order state (call after navigation or showing toast)
     */
    fun resetCreateOrderState() {
        _createOrderState.value = null
    }
    
    /**
     * Validate checkout form inputs
     * @return Pair of (isValid, errorMessage)
     */
    fun validateCheckoutForm(
        shippingAddress: String,
        phoneNumber: String,
        paymentMethod: String
    ): Pair<Boolean, String?> {
        return when {
            shippingAddress.isBlank() -> Pair(false, "Please enter shipping address")
            phoneNumber.isBlank() -> Pair(false, "Please enter phone number")
            phoneNumber.length < 10 -> Pair(false, "Phone number must be at least 10 digits")
            paymentMethod.isBlank() -> Pair(false, "Please select payment method")
            else -> Pair(true, null)
        }
    }
}
