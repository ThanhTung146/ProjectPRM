package com.example.projectprm.data.api.dto

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val address: String
)
