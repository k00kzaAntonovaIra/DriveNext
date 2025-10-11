package com.example.drivenext_antonova

import java.util.*

object RegisterRepository {

    // Enum для пола пользователя
    enum class Gender {
        MALE, FEMALE
    }

    // Data class для хранения всех данных регистрации
    data class UserData(
        // Шаг 1: Основные данные
        var email: String = "",
        var password: String = "",

        // Шаг 2: Личные данные
        var surname: String = "",
        var name: String = "",
        var patronymic: String = "",
        var birthDate: Date? = null,
        var gender: Gender? = null,

        // Шаг 3: Водительские данные
        var licenseNumber: String = "",
        var licenseIssueDate: Date? = null,
        var licensePhoto: String = "",
        var passportPhoto: String = "",
        var profilePhoto: String = ""
    )

    // Текущие данные пользователя
    val currentData = UserData()

    // ==================== МЕТОДЫ ДЛЯ ШАГА 1 ====================

    fun saveEmail(email: String) {
        currentData.email = email.trim()
    }

    fun savePassword(password: String) {
        currentData.password = password
    }

    fun validateStep1(confirmPassword: String, acceptedTerms: Boolean): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        // Валидация email
        if (currentData.email.isEmpty()) {
            errors["email"] = "Введите email"
        } else if (!isValidEmail(currentData.email)) {
            errors["email"] = "Неверный формат email"
        }

        // Валидация пароля
        if (currentData.password.isEmpty()) {
            errors["password"] = "Введите пароль"
        } else if (currentData.password.length < 6) {
            errors["password"] = "Пароль должен быть не менее 6 символов"
        } else if (!containsLetterAndDigit(currentData.password)) {
            errors["password"] = "Пароль должен содержать буквы и цифры"
        }

        // Валидация подтверждения пароля
        if (confirmPassword.isEmpty()) {
            errors["repeat"] = "Повторите пароль"
        } else if (currentData.password != confirmPassword) {
            errors["repeat"] = "Пароли не совпадают"
        }

        // Валидация соглашения
        if (!acceptedTerms) {
            errors["terms"] = "Необходимо принять условия обслуживания"
        }

        return errors
    }

    // ==================== МЕТОДЫ ДЛЯ ШАГА 2 ====================

    fun saveSurname(surname: String) {
        currentData.surname = surname.trim()
    }

    fun saveName(name: String) {
        currentData.name = name.trim()
    }

    fun savePatronymic(patronymic: String) {
        currentData.patronymic = patronymic.trim()
    }

    fun saveBirthDate(date: Date) {
        currentData.birthDate = date
    }

    fun saveGender(gender: Gender) {
        currentData.gender = gender
    }

    fun validateStep2(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        // Валидация фамилии
        if (currentData.surname.isEmpty()) {
            errors["surname"] = "Введите фамилию"
        } else if (currentData.surname.length < 2) {
            errors["surname"] = "Фамилия должна содержать минимум 2 символа"
        } else if (!isValidName(currentData.surname)) {
            errors["surname"] = "Фамилия содержит недопустимые символы"
        }

        // Валидация имени
        if (currentData.name.isEmpty()) {
            errors["name"] = "Введите имя"
        } else if (currentData.name.length < 2) {
            errors["name"] = "Имя должно содержать минимум 2 символа"
        } else if (!isValidName(currentData.name)) {
            errors["name"] = "Имя содержит недопустимые символы"
        }

        // Валидация отчества
        if (currentData.patronymic.isEmpty()) {
            errors["patronymic"] = "Введите отчество"
        } else if (currentData.patronymic.length < 2) {
            errors["patronymic"] = "Отчество должно содержать минимум 2 символа"
        } else if (!isValidName(currentData.patronymic)) {
            errors["patronymic"] = "Отчество содержит недопустимые символы"
        }

        // Валидация даты рождения
        if (currentData.birthDate == null) {
            errors["birthDate"] = "Введите дату рождения"
        } else {
            if (!isAdult(currentData.birthDate!!)) {
                errors["birthDate"] = "Вам должно быть больше 18 лет"
            }
        }

        // Валидация пола
        if (currentData.gender == null) {
            errors["gender"] = "Выберите пол"
        }

        return errors
    }

    // ==================== МЕТОДЫ ДЛЯ ШАГА 3 ====================

    fun saveLicenseNumber(licenseNumber: String) {
        currentData.licenseNumber = licenseNumber.trim()
    }

    fun saveLicenseIssueDate(date: Date) {
        currentData.licenseIssueDate = date
    }

    fun saveLicensePhoto(photoPath: String) {
        currentData.licensePhoto = photoPath
    }

    fun savePassportPhoto(photoPath: String) {
        currentData.passportPhoto = photoPath
    }

    fun saveProfilePhoto(photoPath: String) {
        currentData.profilePhoto = photoPath
    }

    fun validateStep3(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        // Валидация номера водительского удостоверения
        if (currentData.licenseNumber.isEmpty()) {
            errors["licenseNumber"] = "Введите номер водительского удостоверения"
        } else if (currentData.licenseNumber.length < 8) {
            errors["licenseNumber"] = "Номер удостоверения должен содержать минимум 8 символов"
        } else if (!isValidLicenseNumber(currentData.licenseNumber)) {
            errors["licenseNumber"] = "Номер удостоверения содержит недопустимые символы"
        }

        // Валидация даты выдачи
        if (currentData.licenseIssueDate == null) {
            errors["licenseIssueDate"] = "Введите дату выдачи"
        } else {
            if (isFutureDate(currentData.licenseIssueDate!!)) {
                errors["licenseIssueDate"] = "Дата выдачи не может быть в будущем"
            }
        }

        // Валидация фото
        if (currentData.licensePhoto.isEmpty()) {
            errors["licensePhoto"] = "Загрузите фото водительского удостоверения"
        }

        if (currentData.passportPhoto.isEmpty()) {
            errors["passportPhoto"] = "Загрузите фото паспорта"
        }

        return errors
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun containsLetterAndDigit(password: String): Boolean {
        return password.any { it.isLetter() } && password.any { it.isDigit() }
    }

    private fun isValidName(name: String): Boolean {
        return name.matches(Regex("[а-яА-ЯёЁa-zA-Z- ]+"))
    }

    private fun isValidLicenseNumber(license: String): Boolean {
        return license.matches(Regex("[0-9a-zA-Z]+"))
    }

    private fun isAdult(birthDate: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18)
        return birthDate.before(calendar.time) || birthDate == calendar.time
    }

    private fun isFutureDate(date: Date): Boolean {
        return date.after(Date())
    }

    // ==================== ОБЩИЕ МЕТОДЫ ====================

    fun validateAll(): Boolean {
        return validateStep1(currentData.password, true).isEmpty() &&
                validateStep2().isEmpty() &&
                validateStep3().isEmpty()
    }

    fun clearData() {
        currentData.email = ""
        currentData.password = ""
        currentData.surname = ""
        currentData.name = ""
        currentData.patronymic = ""
        currentData.birthDate = null
        currentData.gender = null
        currentData.licenseNumber = ""
        currentData.licenseIssueDate = null
        currentData.licensePhoto = ""
        currentData.passportPhoto = ""
        currentData.profilePhoto = ""
    }

    fun getRegistrationData(): Map<String, Any> {
        return buildMap {
            put("email", currentData.email)
            put("password", currentData.password)
            put("surname", currentData.surname)
            put("name", currentData.name)
            put("patronymic", currentData.patronymic)
            put("birthDate", currentData.birthDate?.time?.toString() ?: "")
            put("gender", currentData.gender?.name ?: "")
            put("licenseNumber", currentData.licenseNumber)
            put("licenseIssueDate", currentData.licenseIssueDate?.time?.toString() ?: "")
            put("licensePhoto", currentData.licensePhoto)
            put("passportPhoto", currentData.passportPhoto)
            put("profilePhoto", currentData.profilePhoto)
        }
    }

    // Методы для проверки заполненности шагов
    fun isStep1Complete(): Boolean {
        return currentData.email.isNotEmpty() && currentData.password.isNotEmpty()
    }

    fun isStep2Complete(): Boolean {
        return currentData.surname.isNotEmpty() &&
                currentData.name.isNotEmpty() &&
                currentData.patronymic.isNotEmpty() &&
                currentData.birthDate != null &&
                currentData.gender != null
    }

    fun isStep3Complete(): Boolean {
        return currentData.licenseNumber.isNotEmpty() &&
                currentData.licenseIssueDate != null &&
                currentData.licensePhoto.isNotEmpty() &&
                currentData.passportPhoto.isNotEmpty()
    }

    fun getProgress(): Int {
        var progress = 0
        if (isStep1Complete()) progress += 33
        if (isStep2Complete()) progress += 33
        if (isStep3Complete()) progress += 34
        return progress
    }
}