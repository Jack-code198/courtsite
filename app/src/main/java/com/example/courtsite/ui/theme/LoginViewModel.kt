package com.example.courtsite.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtsite.data.model.User
import com.example.courtsite.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser.asStateFlow()

    /**
     * Handles user login and saves session if successful.
     */
    fun login(identifier: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = authRepository.loginAndReturnUser(identifier, password)
            if (user != null) {
                _loggedInUser.value = user
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    /**
     * Fetch currently logged-in user details from the database (using session).
     */
    fun loadLoggedInUser() {
        viewModelScope.launch {
            val identifier = authRepository.getLoggedInUserIdentifier()
            if (identifier != null) {
                val user = authRepository.getUserByIdentifier(identifier)
                _loggedInUser.value = user
            }
        }
    }

    /**
     * Get only the identifier (email/phone).
     */
    fun getLoggedInUserIdentifier(): String? {
        return authRepository.getLoggedInUserIdentifier()
    }

    /**
     * Logout and clear session.
     */
    fun logout() {
        authRepository.logout()
        _loggedInUser.value = null
    }
}
