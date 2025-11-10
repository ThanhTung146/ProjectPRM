package com.example.projectprm.data.repository

import com.example.projectprm.data.api.AuthApi
import com.example.projectprm.data.api.dto.LoginRequest
import com.example.projectprm.data.api.dto.RegisterRequest
import com.example.projectprm.data.api.dto.AuthResponse
import com.example.projectprm.data.local.TokenManager
import com.example.projectprm.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    
    suspend fun login(email: String, password: String): Flow<Resource<AuthResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = authApi.login(LoginRequest(email, password))
            
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                // Save token and user info
                tokenManager.saveToken(authResponse.token)
                tokenManager.saveUserInfo(
                    authResponse.user.id,
                    authResponse.user.name,
                    authResponse.user.email
                )
                emit(Resource.Success(authResponse))
            } else {
                emit(Resource.Error(response.message() ?: "Login failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String
    ): Flow<Resource<AuthResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = authApi.register(
                RegisterRequest(fullName, email, password, phoneNumber, address)
            )
            
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                // Save token and user info
                tokenManager.saveToken(authResponse.token)
                tokenManager.saveUserInfo(
                    authResponse.user.id,
                    authResponse.user.name,
                    authResponse.user.email
                )
                emit(Resource.Success(authResponse))
            } else {
                emit(Resource.Error(response.message() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    
    suspend fun logout() {
        tokenManager.clearAll()
    }
    
    fun isLoggedIn(): Flow<Boolean> = flow {
        tokenManager.getToken().collect { token ->
            emit(!token.isNullOrEmpty())
        }
    }
}
