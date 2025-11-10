package com.example.projectprm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectprm.data.api.authApi
import com.example.projectprm.data.api.dto.AuthResponse
import com.example.projectprm.data.local.TokenManager
import com.example.projectprm.data.repository.AuthRepository
import com.example.projectprm.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    
    private val authRepository = AuthRepository(
        application.authApi(),
        TokenManager(application)
    )
    
    private val _loginState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val loginState: StateFlow<Resource<AuthResponse>?> = _loginState
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                _loginState.value = result
            }
        }
    }
    
    fun resetState() {
        _loginState.value = null
    }
}
