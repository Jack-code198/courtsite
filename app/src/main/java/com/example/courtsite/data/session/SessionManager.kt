package com.example.courtsite.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGGED_IN_USER = "logged_in_user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    /**
     * Save the logged-in user identifier (email or phone) and set login status to true.
     */
    fun saveLoggedInUser(identifier: String) {
        sharedPreferences.edit().apply {
            putString(KEY_LOGGED_IN_USER, identifier)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    /**
     * Retrieve the currently logged-in user identifier (email or phone).
     */
    fun getLoggedInUser(): String? {
        return sharedPreferences.getString(KEY_LOGGED_IN_USER, null)
    }

    /**
     * Check if a user is currently logged in.
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Clear login state (log out the user).
     */
    fun logout() {
        sharedPreferences.edit().apply {
            remove(KEY_LOGGED_IN_USER)
            remove(KEY_IS_LOGGED_IN)
            apply()
        }
    }

    /**
     * Legacy method to manually update login state (if needed).
     */
    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            apply()
        }
    }
}
