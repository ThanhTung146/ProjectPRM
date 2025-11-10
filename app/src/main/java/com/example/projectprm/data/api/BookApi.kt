package com.example.projectprm.data.api

import com.example.projectprm.data.model.Book
import com.example.projectprm.data.model.Category
import retrofit2.Response
import retrofit2.http.*

interface BookApi {
    @GET("api/books")
    suspend fun getAllBooks(): Response<List<Book>>
    
    @GET("api/books/{id}")
    suspend fun getBookById(@Path("id") id: Int): Response<Book>
    
    @GET("api/books/search")
    suspend fun searchBooks(@Query("keyword") keyword: String): Response<List<Book>>
    
    @GET("api/books/category/{categoryId}")
    suspend fun getBooksByCategory(@Path("categoryId") categoryId: Int): Response<List<Book>>
    
    @GET("api/categories")
    suspend fun getAllCategories(): Response<List<Category>>
}
