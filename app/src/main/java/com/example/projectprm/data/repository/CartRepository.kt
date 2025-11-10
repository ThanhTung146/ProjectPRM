package com.example.projectprm.data.repository

import com.example.projectprm.data.api.CartApi
import com.example.projectprm.data.api.dto.AddToCartRequest
import com.example.projectprm.data.api.dto.CartItemDto
import com.example.projectprm.data.api.dto.UpdateCartItemRequest
import com.example.projectprm.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository for cart-related operations
 * Handles all cart CRUD operations with the backend API
 */
class CartRepository(
    private val cartApi: CartApi
) {
    
    /**
     * Get all cart items for the current user
     * @return Flow of Resource containing list of cart items
     */
    fun getCartItems(): Flow<Resource<List<CartItemDto>>> = flow {
        try {
            emit(Resource.Loading())
            val response = cartApi.getCart()
            
            if (response.isSuccessful) {
                response.body()?.let { cartItems ->
                    emit(Resource.Success(cartItems))
                } ?: emit(Resource.Error("Empty response from server"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to load cart"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    /**
     * Add a book to cart or update quantity if already exists
     * @param bookId ID of the book to add
     * @param quantity Quantity to add
     * @return Flow of Resource containing success message
     */
    fun addToCart(bookId: Int, quantity: Int): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val request = AddToCartRequest(bookId = bookId, quantity = quantity)
            val response = cartApi.addToCart(request)
            
            if (response.isSuccessful) {
                emit(Resource.Success("Added to cart successfully"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to add to cart"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    /**
     * Update quantity of a cart item
     * @param cartItemId ID of the cart item to update
     * @param quantity New quantity
     * @return Flow of Resource containing updated cart item
     */
    fun updateCartItem(cartItemId: Int, quantity: Int): Flow<Resource<CartItemDto>> = flow {
        try {
            emit(Resource.Loading())
            val request = UpdateCartItemRequest(quantity = quantity)
            val response = cartApi.updateCartItem(cartItemId, request)
            
            if (response.isSuccessful) {
                response.body()?.let { updatedItem ->
                    emit(Resource.Success(updatedItem))
                } ?: emit(Resource.Error("Empty response from server"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to update cart"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    /**
     * Remove an item from cart
     * @param cartItemId ID of the cart item to remove
     * @return Flow of Resource containing success message
     */
    fun removeFromCart(cartItemId: Int): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = cartApi.deleteCartItem(cartItemId)
            
            if (response.isSuccessful) {
                emit(Resource.Success("Item removed from cart"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to remove item"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    /**
     * Clear all items from cart
     * @return Flow of Resource containing success message
     */
    fun clearCart(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = cartApi.clearCart()
            
            if (response.isSuccessful) {
                emit(Resource.Success("Cart cleared successfully"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to clear cart"
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
