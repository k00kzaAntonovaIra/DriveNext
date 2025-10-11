package com.example.drivenext_antonova.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton

class LoginFragment : Fragment() {

    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Используйте правильное имя вашего layout файла
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Найдите элементы по ID из вашего layout
        emailEdit = view.findViewById(R.id.etEmail)
        passwordEdit = view.findViewById(R.id.etPassword)
        loginButton = view.findViewById(R.id.btnLogin)
        registerButton = view.findViewById(R.id.btnRegister)

        if (loginButton != null) {
            loginButton.setOnClickListener {
                Log.d("LoginFragment", "Login button clicked")
                validateAndLogin()
            }
        } else {
            Log.e("LoginFragment", "Login button not found!")
        }

        if (registerButton != null) {
            registerButton.setOnClickListener {
                Log.d("LoginFragment", "Register button clicked")
                try {
                    findNavController().navigate(R.id.action_loginFragment_to_signUpFragment1)
                } catch (e: Exception) {
                    Log.e("LoginFragment", "Navigation to signup failed: ${e.message}", e)
                }
            }
        } else {
            Log.e("LoginFragment", "Register button not found!")
        }

        // Обработка "Забыли пароль?"
        val forgotPassword = view.findViewById<TextView>(R.id.tvForgotPassword)
        if (forgotPassword != null) {
            forgotPassword.setOnClickListener {
                Log.d("LoginFragment", "Forgot password clicked")
                // Обработка восстановления пароля
            }
        } else {
            Log.e("LoginFragment", "Forgot password button not found!")
        }
    }

    private fun validateAndLogin() {
        val email = emailEdit.text.toString().trim()
        val password = passwordEdit.text.toString()

        // Простая валидация
        if (email.isEmpty()) {
            emailEdit.error = "Введите email"
            return
        }

        if (password.isEmpty()) {
            passwordEdit.error = "Введите пароль"
            return
        }

        // Ваша логика входа
        performLogin(email, password)
    }

    private fun performLogin(email: String, password: String) {
        try {
            // Временная заглушка - всегда успешный вход
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Успешный вход - переходим на главный экран
                findNavController().navigate(R.id.action_loginFragment_to_homepageFragment)
            }
        } catch (e: Exception) {
            // Обработка ошибок навигации
            e.printStackTrace()
        }
    }
}