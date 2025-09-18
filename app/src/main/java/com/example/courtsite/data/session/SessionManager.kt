package com.example.courtsite.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGGED_IN_USER = "logged_in_user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
<<<<<<< HEAD
=======
        private const val KEY_BOOKING_COUNT = "booking_count"
        private const val KEY_BOOKING_HOURS = "booking_hours"
        private const val KEY_TOTAL_SPENT = "total_spent"
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
     * Clear session and logout the user.
     */
    fun clearSession() {
        sharedPreferences.edit().apply {
            remove(KEY_LOGGED_IN_USER)
            remove(KEY_IS_LOGGED_IN)
<<<<<<< HEAD
=======
            // Keep stats or clear? We clear on logout
            remove(KEY_BOOKING_COUNT)
            remove(KEY_BOOKING_HOURS)
            remove(KEY_TOTAL_SPENT)
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
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
<<<<<<< HEAD
=======

    // Booking stats accessors
    fun getBookingCount(): Int {
        return sharedPreferences.getInt(KEY_BOOKING_COUNT, 0)
    }

    fun getBookingHours(): Float {
        return sharedPreferences.getFloat(KEY_BOOKING_HOURS, 0f)
    }

    fun getTotalSpent(): Float {
        return sharedPreferences.getFloat(KEY_TOTAL_SPENT, 0f)
    }

    fun addBookingStats(bookings: Int, hours: Float, amount: Float) {
        val currentCount = getBookingCount()
        val currentHours = getBookingHours()
        val currentSpent = getTotalSpent()
        sharedPreferences.edit().apply {
            putInt(KEY_BOOKING_COUNT, currentCount + bookings)
            putFloat(KEY_BOOKING_HOURS, currentHours + hours)
            putFloat(KEY_TOTAL_SPENT, currentSpent + amount)
            apply()
        }
    }
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
}