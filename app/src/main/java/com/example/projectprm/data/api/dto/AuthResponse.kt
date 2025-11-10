package com.example.projectprm.data.api.dto

data class AuthResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val address: String?
)
