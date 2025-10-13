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
import com.example.drivenext_antonova.data.RegisterRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SignUpFragment2 : Fragment() {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastNameLayout = view.findViewById<TextInputLayout>(R.id.lastNameLayout)
        val firstNameLayout = view.findViewById<TextInputLayout>(R.id.firstNameLayout)
        val middleNameLayout = view.findViewById<TextInputLayout>(R.id.middleNameLayout)
        val birthDateLayout = view.findViewById<TextInputLayout>(R.id.birthDateLayout)
        val etLastName = view.findViewById<TextInputEditText>(R.id.etLastName)
        val etFirstName = view.findViewById<TextInputEditText>(R.id.etFirstName)
        val etMiddleName = view.findViewById<TextInputEditText>(R.id.etMiddleName)
        val etBirthDate = view.findViewById<TextInputEditText>(R.id.etBirthDate)
        val rgGender = view.findViewById<android.widget.RadioGroup>(R.id.rgGender)
        val genderError = view.findViewById<TextView>(R.id.genderError)
        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)
        val ivBack = view.findViewById<View>(R.id.ivBack)

        restoreData(etLastName, etFirstName, etMiddleName, etBirthDate, rgGender)

        etBirthDate.setOnClickListener {
            showDatePicker(etBirthDate)
        }

        rgGender.setOnCheckedChangeListener { _, _ ->
            genderError.visibility = View.GONE
        }

        btnNext.setOnClickListener {
            validateAndProceed(
                etLastName, etFirstName, etMiddleName, etBirthDate,
                lastNameLayout, firstNameLayout, middleNameLayout, birthDateLayout,
                rgGender, genderError
            )
        }

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment2_to_signUpFragment1)
        }
    }

    private fun restoreData(
        etLastName: TextInputEditText,
        etFirstName: TextInputEditText,
        etMiddleName: TextInputEditText,
        etBirthDate: TextInputEditText,
        rgGender: android.widget.RadioGroup
    ) {
        etLastName.setText(RegisterRepository.currentData.surname)
        etFirstName.setText(RegisterRepository.currentData.name)
        etMiddleName.setText(RegisterRepository.currentData.patronymic)

        RegisterRepository.currentData.birthDate?.let {
            etBirthDate.setText(dateFormat.format(it))
            selectedDate = it
        }

        when (RegisterRepository.currentData.gender) {
            RegisterRepository.Gender.MALE -> rgGender.check(R.id.rbMale)
            RegisterRepository.Gender.FEMALE -> rgGender.check(R.id.rbFemale)
            else -> rgGender.clearCheck()
        }
    }

    private fun showDatePicker(etBirthDate: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                selectedDate = selectedCalendar.time
                etBirthDate.setText(dateFormat.format(selectedDate!!))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun validateAndProceed(
        etLastName: TextInputEditText,
        etFirstName: TextInputEditText,
        etMiddleName: TextInputEditText,
        etBirthDate: TextInputEditText,
        lastNameLayout: TextInputLayout,
        firstNameLayout: TextInputLayout,
        middleNameLayout: TextInputLayout,
        birthDateLayout: TextInputLayout,
        rgGender: android.widget.RadioGroup,
        genderError: TextView
    ) {

        saveData(etLastName, etFirstName, etMiddleName, rgGender)


        resetErrors(lastNameLayout, firstNameLayout, middleNameLayout, birthDateLayout, genderError)


        val errors = RegisterRepository.validateStep2()

        if (errors.isEmpty()) {

            findNavController().navigate(R.id.action_signUpFragment2_to_signUpFragment3)
        } else {

            showErrors(errors, lastNameLayout, firstNameLayout, middleNameLayout, birthDateLayout, genderError)
        }
    }

    private fun saveData(
        etLastName: TextInputEditText,
        etFirstName: TextInputEditText,
        etMiddleName: TextInputEditText,
        rgGender: android.widget.RadioGroup
    ) {
        RegisterRepository.saveSurname(etLastName.text?.toString()?.trim() ?: "")
        RegisterRepository.saveName(etFirstName.text?.toString()?.trim() ?: "")
        RegisterRepository.savePatronymic(etMiddleName.text?.toString()?.trim() ?: "")
        selectedDate?.let { RegisterRepository.saveBirthDate(it) }

        val gender = when (rgGender.checkedRadioButtonId) {
            R.id.rbMale -> RegisterRepository.Gender.MALE
            R.id.rbFemale -> RegisterRepository.Gender.FEMALE
            else -> null
        }
        gender?.let { RegisterRepository.saveGender(it) }
    }

    private fun resetErrors(
        lastNameLayout: TextInputLayout,
        firstNameLayout: TextInputLayout,
        middleNameLayout: TextInputLayout,
        birthDateLayout: TextInputLayout,
        genderError: TextView
    ) {
        lastNameLayout.error = null
        firstNameLayout.error = null
        middleNameLayout.error = null
        birthDateLayout.error = null
        genderError.visibility = View.GONE
    }

    private fun showErrors(
        errors: Map<String, String>,
        lastNameLayout: TextInputLayout,
        firstNameLayout: TextInputLayout,
        middleNameLayout: TextInputLayout,
        birthDateLayout: TextInputLayout,
        genderError: TextView
    ) {
        errors["surname"]?.let { lastNameLayout.error = it }
        errors["name"]?.let { firstNameLayout.error = it }
        errors["patronymic"]?.let { middleNameLayout.error = it }
        errors["birthDate"]?.let { birthDateLayout.error = it }
        errors["gender"]?.let {
            genderError.text = it
            genderError.visibility = View.VISIBLE
        }
    }
}