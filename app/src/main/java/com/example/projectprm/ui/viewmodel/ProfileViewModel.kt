package com.example.projectprm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectprm.data.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for ProfileScreen
 * Manages user profile data and logout functionality
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val tokenManager = TokenManager(application)
    
    // User information state
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()
    
    // Logout state
    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState: StateFlow<LogoutState> = _logoutState.asStateFlow()
    
    init {
        loadUserInfo()
    }
    
    /**
     * Load user information from TokenManager
     */
    private fun loadUserInfo() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            val email = tokenManager.getEmail()
            val fullName = tokenManager.getFullName()
            val role = tokenManager.getRole()
            
            if (userId != null && email != null) {
                _userInfo.value = UserInfo(
                    userId = userId,
                    email = email,
                    fullName = fullName ?: "User",
                    role = role ?: "CUSTOMER"
                )
            }
        }
    }
    
    /**
     * Logout user and clear all stored data
     */
    fun logout() {
        viewModelScope.launch {
            try {
                _logoutState.value = LogoutState.Loading
                // Clear all user data from TokenManager
                tokenManager.clearToken()
                _logoutState.value = LogoutState.Success
            } catch (e: Exception) {
                _logoutState.value = LogoutState.Error(e.localizedMessage ?: "Logout failed")
            }
        }
    }
    
    /**
     * Reset logout state
     */
    fun resetLogoutState() {
        _logoutState.value = LogoutState.Idle
    }
}

/**
 * Data class representing user information
 */
data class UserInfo(
    val userId: Int,
    val email: String,
    val fullName: String,
    val role: String
)

/**
 * Sealed class representing logout state
 */
sealed class LogoutState {
    object Idle : LogoutState()
    object Loading : LogoutState()
    object Success : LogoutState()
    data class Error(val message: String) : LogoutState()
}
