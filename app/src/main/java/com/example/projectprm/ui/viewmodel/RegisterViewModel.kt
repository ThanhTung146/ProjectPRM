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

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    
    private val authRepository = AuthRepository(
        application.authApi(),
        TokenManager(application)
    )
    
    private val _registerState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val registerState: StateFlow<Resource<AuthResponse>?> = _registerState
    
    fun register(
        fullName: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String
    ) {
        viewModelScope.launch {
            authRepository.register(fullName, email, password, phoneNumber, address).collect { result ->
                _registerState.value = result
            }
        }
    }
    
    fun resetState() {
        _registerState.value = null
    }
}
