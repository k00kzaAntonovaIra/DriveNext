package com.example.drivenext_antonova.util

import android.util.Patterns
import java.util.regex.Pattern

/**
 * Utility class for data validation
 */
object ValidationUtils {
    
    private const val MIN_PASSWORD_LENGTH = 8
    private const val MAX_PASSWORD_LENGTH = 128
    
    // Regex patterns for validation
    private val PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$")
    private val LICENSE_NUMBER_PATTERN = Pattern.compile("^\\d{10}$")
    
    /**
     * Validates email address
     */
    fun isEmailValid(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Validates password strength
     * Requirements: minimum 8 characters, at least one digit
     */
    fun isPasswordStrong(password: String): Boolean {
        if (password.length < MIN_PASSWORD_LENGTH || password.length > MAX_PASSWORD_LENGTH) {
            return false
        }
        return password.any { it.isDigit() }
    }

    /**
     * Checks if passwords match
     */
    fun arePasswordsMatching(password: String, confirm: String): Boolean =
        password == confirm

    /**
     * Validates phone number
     */
    fun isPhoneValid(phone: String): Boolean =
        phone.isNotBlank() && PHONE_PATTERN.matcher(phone).matches()

    /**
     * Validates driver license number (10 digits)
     */
    fun isDriverLicenseValid(licenseNumber: String): Boolean =
        licenseNumber.isNotBlank() && LICENSE_NUMBER_PATTERN.matcher(licenseNumber).matches()

    /**
     * Validates name fields (surname, name, patronymic)
     */
    fun isNameValid(name: String): Boolean {
        return name.isNotBlank() && 
               name.length >= 2 && 
               name.length <= 50 &&
               name.all { it.isLetter() || it == '-' || it == ' ' }
    }

    /**
     * Validates if string is not empty and not just whitespace
     */
    fun isNotBlank(value: String?): Boolean = !value.isNullOrBlank()

    /**
     * Validates if string length is within specified range
     */
    fun isLengthValid(value: String, minLength: Int, maxLength: Int): Boolean =
        value.length in minLength..maxLength
}