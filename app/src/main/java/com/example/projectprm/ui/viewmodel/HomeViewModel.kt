package com.example.projectprm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectprm.data.api.bookApi
import com.example.projectprm.data.model.Book
import com.example.projectprm.data.model.Category
import com.example.projectprm.data.repository.BookRepository
import com.example.projectprm.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val bookRepository = BookRepository(application.bookApi())
    
    private val _booksState = MutableStateFlow<Resource<List<Book>>>(Resource.Loading())
    val booksState: StateFlow<Resource<List<Book>>> = _booksState
    
    private val _categoriesState = MutableStateFlow<Resource<List<Category>>>(Resource.Loading())
    val categoriesState: StateFlow<Resource<List<Category>>> = _categoriesState
    
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId
    
    init {
        loadBooks()
        loadCategories()
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            bookRepository.getAllBooks().collect { result ->
                _booksState.value = result
            }
        }
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            bookRepository.getAllCategories().collect { result ->
                _categoriesState.value = result
            }
        }
    }
    
    fun searchBooks(keyword: String) {
        if (keyword.isBlank()) {
            loadBooks()
            return
        }
        
        viewModelScope.launch {
            bookRepository.searchBooks(keyword).collect { result ->
                _booksState.value = result
            }
        }
    }
    
    fun filterByCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        
        if (categoryId == null) {
            loadBooks()
        } else {
            viewModelScope.launch {
                bookRepository.getBooksByCategory(categoryId).collect { result ->
                    _booksState.value = result
                }
            }
        }
    }
    
    fun refresh() {
        loadBooks()
        loadCategories()
    }
}
