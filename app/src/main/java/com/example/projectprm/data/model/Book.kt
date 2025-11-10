package com.example.projectprm.data.model

data class Book(
    val bookId: Int = 0,
    val title: String = "",
    val author: String = "",
    val categoryId: Int = 0,
    val categoryName: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stockQuantity: Int = 0,
    val isbn: String = "",
    val publisher: String = "",
    val publicationYear: Int = 0,
    val pages: Int = 0,
    val language: String = "English",
    val coverImageUrl: String = "",
    val isActive: Boolean = true,
    val averageRating: Double = 0.0,
    val reviewCount: Int = 0
)
