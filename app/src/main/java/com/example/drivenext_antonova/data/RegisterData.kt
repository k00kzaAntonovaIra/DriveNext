package com.example.drivenext_antonova.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Represents user registration data
 */
@Parcelize
data class RegisterData(
    var email: String = "",
    var password: String = "",
    var surname: String = "",
    var name: String = "",
    var patronymic: String = "",
    var birthDate: Date? = null,
    var gender: Gender? = null,
    var driverLicenseNumber: String = "",
    var driverLicenseIssueDate: Date? = null,
    var profilePhoto: Uri? = null,
    var driverLicensePhoto: Uri? = null,
    var passportPhoto: Uri? = null
) : Parcelable {
    
    /**
     * Returns full name (surname + name + patronymic)
     */
    val fullName: String get() = "$surname $name $patronymic".trim()
    
    /**
     * Returns short name (surname + initials)
     */
    val shortName: String get() {
        val initials = if (name.isNotBlank() && patronymic.isNotBlank()) {
            "${name.first()}.${patronymic.first()}."
        } else if (name.isNotBlank()) {
            "${name.first()}."
        } else {
            ""
        }
        return "$surname $initials".trim()
    }
    
    /**
     * Returns true if all required fields are filled
     */
    val isComplete: Boolean get() = 
        email.isNotBlank() && 
        password.isNotBlank() && 
        surname.isNotBlank() && 
        name.isNotBlank() && 
        patronymic.isNotBlank() && 
        birthDate != null && 
        gender != null && 
        driverLicenseNumber.isNotBlank() && 
        driverLicenseIssueDate != null

    // Aliases for compatibility with UI code
    var licenseNumber: String
        get() = driverLicenseNumber
        set(value) { driverLicenseNumber = value }

    var licenseIssueDate: Date?
        get() = driverLicenseIssueDate
        set(value) { driverLicenseIssueDate = value }
}

/**
 * Represents user gender
 */
enum class Gender(val displayName: String) {
    MALE("Мужской"),
    FEMALE("Женский")
}