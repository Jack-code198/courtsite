package com.example.courtsite.data.repository

import com.example.courtsite.data.session.SessionManager
import com.example.courtsite.data.db.UserDao
import com.example.courtsite.data.model.User

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {

    /**
     * Basic login (returns Boolean only)
     */
    suspend fun login(identifier: String, password: String): Boolean {
        val user = userDao.findUser(identifier, password)
        return if (user != null) {
            sessionManager.saveLoggedInUser(identifier)
            true
        } else {
            false
        }
    }

    /**
     * Enhanced login: returns full User object if successful
     */
    suspend fun loginAndReturnUser(identifier: String, password: String): User? {
        val user = userDao.findUser(identifier, password)
        if (user != null) {
            sessionManager.saveLoggedInUser(identifier)
        }
        return user
    }

    /**
     * Fetch user details using stored identifier (for profile, etc.)
     */
    suspend fun getUserByIdentifier(identifier: String): User? {
        return userDao.findUserByIdentifier(identifier)
    }

    /**
     * Get only the logged-in identifier (email/phone)
     */
    fun getLoggedInUserIdentifier(): String? {
        return sessionManager.getLoggedInUser()
    }

    /**
     * Logout user
     */
    fun logout() {
        sessionManager.logout()
    }
}
