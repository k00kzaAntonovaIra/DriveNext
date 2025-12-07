package com.example.drivenext_antonova.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var btnAddPhoto: ImageButton
    private lateinit var tvUserName: TextView
    private lateinit var tvJoinedValue: TextView
    private lateinit var tvEmailValue: TextView
    private lateinit var tvChangePassword: TextView
    private lateinit var tvGenderValue: TextView
    private lateinit var tvGoogleValue: TextView
    private lateinit var tvLogout: TextView
    private lateinit var ivEmailEdit: ImageView
    private lateinit var ivGenderEdit: ImageView
    private lateinit var ivGoogleEdit: ImageView

    private lateinit var ivHome: ImageView
    private lateinit var ivNotes: ImageView
    private lateinit var ivSettings: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddPhoto = view.findViewById(R.id.btnAddPhoto)
        tvUserName = view.findViewById(R.id.tvUserName)
        tvJoinedValue = view.findViewById(R.id.tvJoinedValue)
        tvEmailValue = view.findViewById(R.id.tvEmailValue)
        tvChangePassword = view.findViewById(R.id.tvChangePassword)
        tvGenderValue = view.findViewById(R.id.tvGenderValue)
        tvGoogleValue = view.findViewById(R.id.tvGoogleValue)
        tvLogout = view.findViewById(R.id.tvLogout)
        ivEmailEdit = view.findViewById(R.id.ivEmailEdit)
        ivGenderEdit = view.findViewById(R.id.ivGenderEdit)
        ivGoogleEdit = view.findViewById(R.id.ivGoogleEdit)

        ivHome = view.findViewById(R.id.ivHome)
        ivNotes = view.findViewById(R.id.ivNotes)
        ivSettings = view.findViewById(R.id.ivSettings)

        setupListeners()
        loadUserInfo()
    }

    private fun setupListeners() {
        btnAddPhoto.setOnClickListener { Toast.makeText(requireContext(), "Выбор фото профиля", Toast.LENGTH_SHORT).show() }
        tvChangePassword.setOnClickListener { showPasswordChangeDialog() }
        ivGoogleEdit.setOnClickListener { showGoogleEditDialog() }
        ivEmailEdit.setOnClickListener { showEmailEditDialog() }
        ivGenderEdit.setOnClickListener { showGenderEditDialog() }
        tvLogout.setOnClickListener { performLogout() }

        ivHome.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_homepageFragment) }
        ivNotes.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_bookmarksFragment) }
        ivSettings.setOnClickListener { /* already here */ }
    }

    private fun loadUserInfo() {
        val prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val email = prefs.getString("user_email", "")
        val firstName = prefs.getString("firstName", "")
        val lastName = prefs.getString("lastName", "")
        val gender = prefs.getString("gender", "")
        val registrationTimestamp = prefs.getLong("registration_timestamp", 0L)

        val fullName = if (firstName.isNullOrEmpty() || lastName.isNullOrEmpty()) {
            if (!email.isNullOrEmpty()) email.substringBefore('@').replaceFirstChar { it.uppercaseChar() } else getString(R.string.user)
        } else "$firstName $lastName"
        tvUserName.text = fullName
        tvEmailValue.text = email

        val genderText = when (gender) {
            "male" -> getString(R.string.gender_male)
            "female" -> getString(R.string.gender_female)
            else -> getString(R.string.gender_unknown)
        }
        tvGenderValue.text = genderText

        if (registrationTimestamp > 0) {
            val dateFormat = SimpleDateFormat("LLLL yyyy", Locale.getDefault())
            tvJoinedValue.text = getString(R.string.joined_in, dateFormat.format(Date(registrationTimestamp)))
        } else {
            tvJoinedValue.text = getString(R.string.joined_in, "июле 2024")
        }

        tvGoogleValue.text = "ivanov@gmail.com"
    }

    private fun performLogout() {
        val prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Toast.makeText(requireContext(), R.string.logged_out, Toast.LENGTH_SHORT).show()
        val navController = findNavController()
        try {
            val options = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            navController.navigate(R.id.loginFragment, null, options)
        } catch (_: Exception) {
            try { navController.navigate(R.id.loginFragment) } catch (_: Exception) {}
        }
    }

    private fun showEmailEditDialog() {
        val currentEmail = tvEmailValue.text.toString()
        val input = android.widget.EditText(requireContext())
        input.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        input.setText(currentEmail)
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Изменить email")
            .setMessage("Введите новый email")
            .setView(input)
            .setPositiveButton("Сохранить") { _, _ ->
                val newEmail = input.text.toString().trim()
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    val prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    prefs.edit().putString("user_email", newEmail).apply()
                    tvEmailValue.text = newEmail
                    Toast.makeText(requireContext(), "Email обновлен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Неверный формат email", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showGenderEditDialog() {
        val genders = arrayOf("Мужской", "Женский")
        val currentGender = tvGenderValue.text.toString()
        var selectedIndex = genders.indexOf(currentGender)
        if (selectedIndex == -1) selectedIndex = 0
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Выберите пол")
            .setSingleChoiceItems(genders, selectedIndex) { dialog, which ->
                val newGender = genders[which]
                val prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                prefs.edit().putString("gender", if (which == 0) "male" else "female").apply()
                tvGenderValue.text = newGender
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showPasswordChangeDialog() {
        val layout = android.widget.LinearLayout(requireContext())
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val currentPasswordInput = android.widget.EditText(requireContext())
        currentPasswordInput.hint = "Текущий пароль"
        currentPasswordInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        currentPasswordInput.setSingleLine(true)

        val newPasswordInput = android.widget.EditText(requireContext())
        newPasswordInput.hint = "Новый пароль"
        newPasswordInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        newPasswordInput.setSingleLine(true)

        val confirmPasswordInput = android.widget.EditText(requireContext())
        confirmPasswordInput.hint = "Подтвердите новый пароль"
        confirmPasswordInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        confirmPasswordInput.setSingleLine(true)

        layout.addView(currentPasswordInput)
        layout.addView(newPasswordInput)
        layout.addView(confirmPasswordInput)

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Изменить пароль")
            .setView(layout)
            .setPositiveButton("Сохранить") { _, _ ->
                val currentPassword = currentPasswordInput.text.toString().trim()
                val newPassword = newPasswordInput.text.toString().trim()
                val confirmPassword = confirmPasswordInput.text.toString().trim()
                val prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                val savedPassword = prefs.getString("password", "")
                when {
                    currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                        Toast.makeText(requireContext(), "Все поля обязательны", Toast.LENGTH_SHORT).show()
                    }
                    currentPassword != savedPassword -> {
                        Toast.makeText(requireContext(), "Неверный текущий пароль", Toast.LENGTH_SHORT).show()
                    }
                    newPassword.length < 8 -> {
                        Toast.makeText(requireContext(), "Пароль должен содержать минимум 8 символов", Toast.LENGTH_SHORT).show()
                    }
                    newPassword != confirmPassword -> {
                        Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        prefs.edit().putString("password", newPassword).apply()
                        Toast.makeText(requireContext(), "Пароль обновлен", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showGoogleEditDialog() {
        val currentGoogle = tvGoogleValue.text.toString()
        val input = android.widget.EditText(requireContext())
        input.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        input.setText(currentGoogle)
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Изменить Google аккаунт")
            .setMessage("Введите новый Google email")
            .setView(input)
            .setPositiveButton("Сохранить") { _, _ ->
                val newGoogle = input.text.toString().trim()
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(newGoogle).matches()) {
                    tvGoogleValue.text = newGoogle
                    Toast.makeText(requireContext(), "Google аккаунт обновлен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Неверный формат email", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}