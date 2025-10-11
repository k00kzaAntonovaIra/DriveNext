package com.example.drivenext_antonova.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.example.drivenext_antonova.RegisterRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SignUpFragment3 : Fragment() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private var selectedLicenseDate: Date? = null

    private lateinit var etLicense: TextInputEditText
    private lateinit var etLicenseDate: TextInputEditText
    private lateinit var licenseLayout: TextInputLayout
    private lateinit var licenseDateLayout: TextInputLayout
    private lateinit var photoError: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Находим все элементы
        etLicense = view.findViewById(R.id.etLicense)
        etLicenseDate = view.findViewById(R.id.etLicenseDate)
        licenseLayout = view.findViewById(R.id.licenseLayout)
        licenseDateLayout = view.findViewById(R.id.licenseDateLayout)
        photoError = view.findViewById(R.id.photoError)

        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)
        val ivBack = view.findViewById<View>(R.id.ivBack)
        val btnUploadLicense = view.findViewById<View>(R.id.btnUploadLicense)
        val btnUploadPassport = view.findViewById<View>(R.id.btnUploadPassport)
        val ivProfilePhoto = view.findViewById<View>(R.id.ivProfilePhoto)

        // Восстанавливаем данные
        restoreData()

        // Обработчики кликов для загрузки фото (заглушки)
        btnUploadLicense.setOnClickListener {
            // Временная заглушка - отмечаем что фото загружено
            RegisterRepository.saveLicensePhoto("license_photo_path")
            showUploadSuccess("Фото водительского удостоверения загружено")
        }

        btnUploadPassport.setOnClickListener {
            // Временная заглушка - отмечаем что фото загружено
            RegisterRepository.savePassportPhoto("passport_photo_path")
            showUploadSuccess("Фото паспорта загружено")
        }

        ivProfilePhoto.setOnClickListener {
            // Временная заглушка для аватара
            showUploadSuccess("Фото профиля загружено")
        }

        // Обработчик выбора даты
        etLicenseDate.setOnClickListener {
            showDatePicker()
        }

        // Кнопка "Далее"
        btnNext.setOnClickListener {
            validateAndProceed()
        }

        // Кнопка "Назад"
        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment3_to_signUpFragment2)
        }
    }

    private fun restoreData() {
        etLicense.setText(RegisterRepository.currentData.licenseNumber)
        RegisterRepository.currentData.licenseIssueDate?.let {
            selectedLicenseDate = it
            etLicenseDate.setText(dateFormat.format(it))
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                selectedLicenseDate = selectedCalendar.time
                etLicenseDate.setText(dateFormat.format(selectedLicenseDate!!))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun validateAndProceed() {
        // Сохраняем данные
        saveData()

        // Сбрасываем ошибки
        resetErrors()

        // Валидация
        val errors = RegisterRepository.validateStep3()

        if (errors.isEmpty()) {
            // Успешно - переходим дальше
            navigateToSuccess()
        } else {
            // Показываем ошибки
            showErrors(errors)
        }
    }

    private fun saveData() {
        RegisterRepository.saveLicenseNumber(etLicense.text?.toString()?.trim() ?: "")
        selectedLicenseDate?.let { RegisterRepository.saveLicenseIssueDate(it) }
    }

    private fun resetErrors() {
        licenseLayout.error = null
        licenseDateLayout.error = null
        photoError.visibility = View.GONE
    }

    private fun showErrors(errors: Map<String, String>) {
        var hasErrors = false

        errors["licenseNumber"]?.let {
            licenseLayout.error = it
            hasErrors = true
        }

        errors["licenseIssueDate"]?.let {
            licenseDateLayout.error = it
            hasErrors = true
        }

        // Проверка фото
        val missingLicensePhoto = errors.containsKey("licensePhoto")
        val missingPassportPhoto = errors.containsKey("passportPhoto")

        if (missingLicensePhoto || missingPassportPhoto) {
            photoError.text = "Пожалуйста, загрузите все необходимые фото"
            photoError.visibility = View.VISIBLE
            hasErrors = true
        }
    }

    private fun navigateToSuccess() {
        try {
            findNavController().navigate(R.id.action_signUpFragment3_to_signUpSuccessFragment)
        } catch (e: Exception) {
            // Временное решение если навигация не настроена
            android.widget.Toast.makeText(
                requireContext(),
                "Регистрация завершена!",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showUploadSuccess(message: String) {
        android.widget.Toast.makeText(
            requireContext(),
            message,
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}