package com.example.drivenext_antonova.ui.authorization

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.example.drivenext_antonova.data.RegisterRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
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
    private lateinit var ivProfilePhoto: ImageView
    private lateinit var btnUploadLicense: ImageView
    private lateinit var btnUploadPassport: ImageView


    companion object {
        private const val REQUEST_CODE_PROFILE = 1001
        private const val REQUEST_CODE_LICENSE = 1002
        private const val REQUEST_CODE_PASSPORT = 1003
    }

    // Переменная для отслеживания текущего типа фото
    private var currentPhotoType: Int = REQUEST_CODE_PROFILE

    // Launcher для выбора изображений из галереи
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { handleImageSelection(it) }
    }

    // Launcher для съемки фото камерой
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            currentCameraUri?.let { handleImageSelection(it) }
        }
    }

    // URI для временного файла камеры
    private var currentCameraUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        etLicense = view.findViewById(R.id.etLicense)
        etLicenseDate = view.findViewById(R.id.etLicenseDate)
        licenseLayout = view.findViewById(R.id.licenseLayout)
        licenseDateLayout = view.findViewById(R.id.licenseDateLayout)
        photoError = view.findViewById(R.id.photoError)
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto)
        btnUploadLicense = view.findViewById(R.id.btnUploadLicense)
        btnUploadPassport = view.findViewById(R.id.btnUploadPassport)

        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)
        val ivBack = view.findViewById<View>(R.id.ivBack)

        restoreData()

        btnUploadLicense.setOnClickListener {
            currentPhotoType = REQUEST_CODE_LICENSE
            showImageSourceDialog()
        }

        btnUploadPassport.setOnClickListener {
            currentPhotoType = REQUEST_CODE_PASSPORT
            showImageSourceDialog()
        }

        ivProfilePhoto.setOnClickListener {
            currentPhotoType = REQUEST_CODE_PROFILE
            showImageSourceDialog()
        }


        etLicenseDate.setOnClickListener {
            showDatePicker()
        }


        btnNext.setOnClickListener {
            validateAndProceed()
        }


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

            navigateToSuccess()
        } else {

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
            Toast.makeText(
                requireContext(),
                "Регистрация завершена!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showUploadSuccess(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    // Методы для работы с выбором источника изображения
    private fun showImageSourceDialog() {
        val options = arrayOf("Камера", "Галерея")

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите источник")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun openCamera() {
        try {
            val photoFile = createImageFile()
            currentCameraUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                photoFile
            )
            cameraLauncher.launch(currentCameraUri)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Ошибка при создании файла для фото: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"

        // Создаем папку Pictures если её нет
        val storageDir = File(requireContext().getExternalFilesDir(null), "Pictures")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun handleImageSelection(uri: Uri) {
        when (currentPhotoType) {
            REQUEST_CODE_PROFILE -> {
                ivProfilePhoto.setImageURI(uri)
                RegisterRepository.saveProfilePhoto(uri.toString())
                showUploadSuccess("Фото профиля загружено")
            }
            REQUEST_CODE_LICENSE -> {
                RegisterRepository.saveLicensePhoto(uri.toString())
                showUploadSuccess("Фото водительского удостоверения загружено")
            }
            REQUEST_CODE_PASSPORT -> {
                RegisterRepository.savePassportPhoto(uri.toString())
                showUploadSuccess("Фото паспорта загружено")
            }
        }
    }
}