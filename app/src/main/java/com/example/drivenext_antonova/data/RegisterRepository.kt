package com.example.drivenext_antonova.data

import android.net.Uri
import com.example.drivenext_antonova.util.ValidationUtils
import java.util.*
import androidx.core.net.toUri

/**
 * Repository for user registration and authentication
 */
object RegisterRepository {

    private val _currentData = RegisterData()
    val currentData: RegisterData
        get() = _currentData

    // Data saving methods
    fun saveEmail(email: String) { _currentData.email = email.trim() }
    fun savePassword(password: String) { _currentData.password = password }
    fun saveSurname(surname: String) { _currentData.surname = surname.trim() }
    fun saveName(name: String) { _currentData.name = name.trim() }
    fun savePatronymic(patronymic: String) { _currentData.patronymic = patronymic.trim() }
    fun saveBirthDate(date: Date) { _currentData.birthDate = date }
    fun saveGender(gender: Gender) { _currentData.gender = gender }
    fun saveDriverLicenseNumber(number: String) { _currentData.driverLicenseNumber = number.trim() }
    fun saveDriverLicenseIssueDate(date: Date) { _currentData.driverLicenseIssueDate = date }
    fun saveDriverLicensePhoto(uri: Uri) { _currentData.driverLicensePhoto = uri }
    fun savePassportPhoto(uri: Uri) { _currentData.passportPhoto = uri }
    fun saveProfilePhoto(uri: Uri) { _currentData.profilePhoto = uri }
    // Aliases for backward compatibility
    fun saveLicenseNumber(number: String) = saveDriverLicenseNumber(number)
    fun saveLicenseIssueDate(date: Date) = saveDriverLicenseIssueDate(date)
    fun saveLicensePhoto(uri: Uri) = saveDriverLicensePhoto(uri)
    fun saveLicensePhoto(uri: String) = saveDriverLicensePhoto(uri.toUri())
    fun savePassportPhoto(uri: String) { savePassportPhoto(uri.toUri()) }
    fun saveProfilePhoto(uri: String) { saveProfilePhoto(uri.toUri()) }
    
    /**
     * Gets current registration data
     */
    // getCurrentData kept previously; property currentData can be used instead
    
    /**
     * Clears current registration data
     */
    fun clearCurrentData() {
        _currentData.email = ""
        _currentData.password = ""
        _currentData.surname = ""
        _currentData.name = ""
        _currentData.patronymic = ""
        _currentData.birthDate = null
        _currentData.gender = null
        _currentData.driverLicenseNumber = ""
        _currentData.driverLicenseIssueDate = null
        _currentData.profilePhoto = null
        _currentData.driverLicensePhoto = null
        _currentData.passportPhoto = null
    }

    // Field validation methods
    fun isSurnameValid(): Boolean = currentData.surname.isNotBlank()
    fun isNameValid(): Boolean = currentData.name.isNotBlank()
    fun isPatronymicValid(): Boolean = currentData.patronymic.isNotBlank()
    fun isBirthDateValid(): Boolean = currentData.birthDate != null
    fun isGenderValid(): Boolean = currentData.gender != null

    fun isDriverLicenseNumberValid(): Boolean = currentData.driverLicenseNumber.length == 10
    fun isDriverLicenseIssueDateValid(): Boolean = currentData.driverLicenseIssueDate != null
    fun isDriverLicensePhotoValid(): Boolean = currentData.driverLicensePhoto != null
    fun isPassportPhotoValid(): Boolean = currentData.passportPhoto != null

    // Validation methods
    fun validateStep1(confirmPassword: String, acceptedTerms: Boolean): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (currentData.email.isBlank()) {
            errors["email"] = "Это поле является обязательным."
        } else if (!ValidationUtils.isEmailValid(currentData.email)) {
            errors["email"] = "Введите корректный адрес электронной почты."
        } else if (isEmailTaken(currentData.email)) {
            errors["email"] = "Этот email уже занят."
        }

        if (currentData.password.isBlank()) {
            errors["password"] = "Это поле является обязательным"
        } else if (!ValidationUtils.isPasswordStrong(currentData.password)) {
            errors["password"] = "Пароль должен быть не менее 8 символов и содержать хотя бы одну цифру."
        }

        if (!ValidationUtils.arePasswordsMatching(currentData.password, confirmPassword)) {
            errors["repeat"] = "Пароли не совпадают."
        }

        if (!acceptedTerms) {
            errors["terms"] = "Необходимо согласиться с условиями обслуживания и политикой конфиденциальности."
        }

        return errors
    }

    fun validateStep2(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (!isSurnameValid()) errors["surname"] = "Это поле является обязательным"
        if (!isNameValid()) errors["name"] = "Это поле является обязательным"
        if (!isPatronymicValid()) errors["patronymic"] = "Это поле является обязательным"
        if (!isBirthDateValid()) errors["birthDate"] = "Введите корректную дату рождения"
        if (!isGenderValid()) errors["gender"] = "Выберите пол"

        return errors
    }

    fun validateStep3(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (currentData.driverLicenseNumber.isBlank()) {
            errors["licenseNumber"] = "Это поле является обязательным"
        } else if (!isDriverLicenseNumberValid()) {
            errors["licenseNumber"] = "Введите корректный номер водительского удостоверения"
        }

        if (!isDriverLicenseIssueDateValid()) errors["licenseDate"] = "Введите корректную дату выдачи"
        if (!isDriverLicensePhotoValid()) errors["licensePhoto"] = "Загрузите фото водительского удостоверения"
        if (!isPassportPhotoValid()) errors["passportPhoto"] = "Загрузите фото паспорта"

        return errors
    }

    // User database simulation
    private val users = mutableListOf<RegisterData>()

    private val testUsers = listOf(
        RegisterData(
            email = "ivan.ivanov@mail.com",
            password = "pass1234",
            surname = "Иванов",
            name = "Иван",
            patronymic = "Сергеевич",
            birthDate = Date(95, 4, 12), // 12 мая 1995
            gender = Gender.MALE,
            driverLicenseNumber = "1234567890",
            driverLicenseIssueDate = Date(120, 6, 5), // 5 июля 2020
            profilePhoto = "android.resource://com.example.drivenext_antonova/drawable/profile_ivan".toUri(),
        ),
        RegisterData(
            email = "anna.petrova@mail.com",
            password = "qwerty123",
            surname = "Петрова",
            name = "Анна",
            patronymic = "Игоревна",
            birthDate = Date(98, 10, 3), // 3 ноября 1998
            gender = Gender.FEMALE,
            driverLicenseNumber = "0987654321",
            driverLicenseIssueDate = Date(122, 2, 14), // 14 марта 2022
        )
    )

    var currentUser: RegisterData? = null
        private set

    /**
     * Checks if email is already taken
     */
    fun isEmailTaken(email: String): Boolean {
        return users.any { it.email.equals(email, true) }
    }

    /**
     * Adds test users to the database
     */
    fun addTestUsers() {
        if (users.isEmpty()) {
            users.addAll(testUsers)
        }
    }

    /**
     * Finds user by email
     */
    fun findUserByEmail(email: String): RegisterData? {
        val trimmedEmail = email.trim()
        return users.find { it.email.trim().equals(trimmedEmail, ignoreCase = true) }
    }

    /**
     * Sets current user
     */
    fun setCurrentUser(user: RegisterData?) {
        currentUser = user
    }

    /**
     * Adds new user to database
     */
    fun addUser(user: RegisterData): Boolean {
        return if (!isEmailTaken(user.email)) {
            users.add(user)
            true
        } else {
            false
        }
    }

    /**
     * Attempts to login user
     */
    fun loginUser(email: String, password: String): Boolean {
        val trimmedEmail = email.trim()
        val user = users.find { 
            it.email.trim().equals(trimmedEmail, ignoreCase = true) && 
            it.password == password 
        }
        return if (user != null) {
            currentUser = user
            true
        } else {
            false
        }
    }

    /**
     * Logs out current user
     */
    fun logout() {
        currentUser = null
    }

    /**
     * Gets all users (for testing purposes)
     */
    fun getAllUsers(): List<RegisterData> = users.toList()
}