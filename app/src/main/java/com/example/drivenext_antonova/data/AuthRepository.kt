package com.example.drivenext_antonova.data

import android.content.Context
import com.example.drivenext_antonova.util.PrefsManager

/**
 * Repository for authentication operations
 */
object AuthRepository {

    private var prefs: PrefsManager? = null

    /**
     * Initializes the authentication repository
     */
    fun init(context: Context) {
        prefs = PrefsManager(context)
        // Add test users on first launch
        RegisterRepository.addTestUsers()

        // Try to restore current user by token
        val token = prefs?.getAccessToken()
        if (!token.isNullOrBlank()) {
            // If user with this email exists - set as current user
            RegisterRepository.findUserByEmail(token)?.let { user ->
                RegisterRepository.setCurrentUser(user)
            }
        }
    }

    /**
     * Attempts to login user
     */
    fun login(email: String, password: String): Boolean {
        val success = RegisterRepository.loginUser(email, password)
        if (success) {
            // Save token (using email as token for now)
            prefs?.saveAccessToken(email)
        }
        return success
    }

    /**
     * Logs out current user
     */
    fun logout() {
        prefs?.clearAccessToken()
        RegisterRepository.logout()
    }

    /**
     * Gets current logged in user
     */
    fun getCurrentUser(): RegisterData? {
        return RegisterRepository.currentUser
    }

    /**
     * Checks if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return getCurrentUser() != null && prefs?.isAccessTokenValid() == true
    }

    /**
     * Gets access token
     */
    fun getAccessToken(): String? {
        return prefs?.getAccessToken()
    }
}