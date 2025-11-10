package com.example.projectprm.data.api.dto

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
