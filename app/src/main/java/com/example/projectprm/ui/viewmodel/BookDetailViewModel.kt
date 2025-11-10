package com.example.projectprm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectprm.data.api.bookApi
import com.example.projectprm.data.api.cartApi
import com.example.projectprm.data.api.dto.AddToCartRequest
import com.example.projectprm.data.model.Book
import com.example.projectprm.data.repository.BookRepository
import com.example.projectprm.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val bookRepository = BookRepository(application.bookApi())
    private val cartApi = application.cartApi()
    
    private val _bookState = MutableStateFlow<Resource<Book>>(Resource.Loading())
    val bookState: StateFlow<Resource<Book>> = _bookState
    
    private val _addToCartState = MutableStateFlow<Resource<String>?>(null)
    val addToCartState: StateFlow<Resource<String>?> = _addToCartState
    
    fun loadBook(bookId: Int) {
        viewModelScope.launch {
            bookRepository.getBookById(bookId).collect { result ->
                _bookState.value = result
            }
        }
    }
    
    fun addToCart(bookId: Int, quantity: Int) {
        viewModelScope.launch {
            try {
                _addToCartState.value = Resource.Loading()
                val response = cartApi.addToCart(AddToCartRequest(bookId, quantity))
                
                if (response.isSuccessful) {
                    _addToCartState.value = Resource.Success("Added to cart successfully")
                } else {
                    _addToCartState.value = Resource.Error(response.message() ?: "Failed to add to cart")
                }
            } catch (e: Exception) {
                _addToCartState.value = Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
    
    fun resetAddToCartState() {
        _addToCartState.value = null
    }
}
