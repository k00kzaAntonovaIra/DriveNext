package com.example.drivenext_antonova

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Для проверки первого запуска
    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean("is_first_launch", true)
    }

    fun setFirstLaunchCompleted() {
        prefs.edit().putBoolean("is_first_launch", false).apply()
    }

    // Для проверки токена
    fun isAccessTokenValid(): Boolean {
        val token = prefs.getString("access_token", null)
        return !token.isNullOrEmpty()
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun clearAccessToken() {
        prefs.edit().remove("access_token").apply()
    }

    // Дополнительные полезные методы
    fun saveUserId(userId: String) {
        prefs.edit().putString("user_id", userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}