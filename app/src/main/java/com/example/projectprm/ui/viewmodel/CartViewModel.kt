package com.example.projectprm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectprm.data.api.dto.CartItemDto
import com.example.projectprm.data.repository.CartRepository
import com.example.projectprm.data.util.Resource
import com.example.projectprm.util.cartApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for CartScreen
 * Manages cart items state and operations (load, update, remove, clear)
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {
    
    private val cartRepository = CartRepository(application.cartApi())
    
    // Cart items state
    private val _cartItemsState = MutableStateFlow<Resource<List<CartItemDto>>>(Resource.Loading())
    val cartItemsState: StateFlow<Resource<List<CartItemDto>>> = _cartItemsState.asStateFlow()
    
    // Update cart item state
    private val _updateItemState = MutableStateFlow<Resource<CartItemDto>?>(null)
    val updateItemState: StateFlow<Resource<CartItemDto>?> = _updateItemState.asStateFlow()
    
    // Remove item state
    private val _removeItemState = MutableStateFlow<Resource<String>?>(null)
    val removeItemState: StateFlow<Resource<String>?> = _removeItemState.asStateFlow()
    
    // Clear cart state
    private val _clearCartState = MutableStateFlow<Resource<String>?>(null)
    val clearCartState: StateFlow<Resource<String>?> = _clearCartState.asStateFlow()
    
    init {
        loadCartItems()
    }
    
    /**
     * Load all cart items
     */
    fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { resource ->
                _cartItemsState.value = resource
            }
        }
    }
    
    /**
     * Update quantity of a cart item
     * @param cartItemId ID of the cart item
     * @param quantity New quantity
     */
    fun updateQuantity(cartItemId: Int, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItem(cartItemId, quantity).collect { resource ->
                _updateItemState.value = resource
                // Reload cart items after successful update
                if (resource is Resource.Success) {
                    loadCartItems()
                }
            }
        }
    }
    
    /**
     * Remove an item from cart
     * @param cartItemId ID of the cart item to remove
     */
    fun removeItem(cartItemId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItemId).collect { resource ->
                _removeItemState.value = resource
                // Reload cart items after successful removal
                if (resource is Resource.Success) {
                    loadCartItems()
                }
            }
        }
    }
    
    /**
     * Clear all items from cart
     */
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart().collect { resource ->
                _clearCartState.value = resource
                // Reload cart items after successful clear
                if (resource is Resource.Success) {
                    loadCartItems()
                }
            }
        }
    }
    
    /**
     * Calculate total amount from cart items
     * @return Total amount in VND
     */
    fun calculateTotalAmount(): Double {
        return when (val state = _cartItemsState.value) {
            is Resource.Success -> {
                state.data?.sumOf { it.book.price * it.quantity } ?: 0.0
            }
            else -> 0.0
        }
    }
    
    /**
     * Get total number of items in cart
     * @return Total quantity
     */
    fun getTotalItemCount(): Int {
        return when (val state = _cartItemsState.value) {
            is Resource.Success -> {
                state.data?.sumOf { it.quantity } ?: 0
            }
            else -> 0
        }
    }
    
    /**
     * Reset update item state (call after showing toast)
     */
    fun resetUpdateItemState() {
        _updateItemState.value = null
    }
    
    /**
     * Reset remove item state (call after showing toast)
     */
    fun resetRemoveItemState() {
        _removeItemState.value = null
    }
    
    /**
     * Reset clear cart state (call after showing toast)
     */
    fun resetClearCartState() {
        _clearCartState.value = null
    }
}
