package com.example.projectprm.data.repository

import com.example.projectprm.data.api.BookApi
import com.example.projectprm.data.model.Book
import com.example.projectprm.data.model.Category
import com.example.projectprm.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookRepository(
    private val bookApi: BookApi
) {
    
    suspend fun getAllBooks(): Flow<Resource<List<Book>>> = flow {
        try {
            emit(Resource.Loading())
            val response = bookApi.getAllBooks()
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch books"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    suspend fun getBookById(bookId: Int): Flow<Resource<Book>> = flow {
        try {
            emit(Resource.Loading())
            val response = bookApi.getBookById(bookId)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch book details"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    suspend fun searchBooks(keyword: String): Flow<Resource<List<Book>>> = flow {
        try {
            emit(Resource.Loading())
            val response = bookApi.searchBooks(keyword)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(response.message() ?: "Search failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    suspend fun getBooksByCategory(categoryId: Int): Flow<Resource<List<Book>>> = flow {
        try {
            emit(Resource.Loading())
            val response = bookApi.getBooksByCategory(categoryId)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch books by category"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    suspend fun getAllCategories(): Flow<Resource<List<Category>>> = flow {
        try {
            emit(Resource.Loading())
            val response = bookApi.getAllCategories()
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch categories"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
