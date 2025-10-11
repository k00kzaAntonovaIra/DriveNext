package com.example.drivenext_antonova.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton

class GettingStartedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_getting_started, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("GettingStartedFragment", "onViewCreated called")

        val loginButton = view.findViewById<MaterialButton>(R.id.btnLogin)
        val registerButton = view.findViewById<MaterialButton>(R.id.btnRegister)

        Log.d("GettingStartedFragment", "Login button found: ${loginButton != null}")
        Log.d("GettingStartedFragment", "Register button found: ${registerButton != null}")

        if (loginButton != null) {
            loginButton.setOnClickListener {
                Log.d("GettingStartedFragment", "Login button clicked - starting navigation")
                try {
                    val navController = findNavController()
                    Log.d("GettingStartedFragment", "NavController found: ${navController != null}")
                    Log.d("GettingStartedFragment", "Current destination: ${navController.currentDestination?.id}")
                    
                    navController.navigate(R.id.action_gettingStartedFragment_to_loginFragment)
                    Log.d("GettingStartedFragment", "Navigation to login successful")
                } catch (e: Exception) {
                    Log.e("GettingStartedFragment", "Navigation to login failed: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("GettingStartedFragment", "Login button not found!")
        }

        if (registerButton != null) {
            registerButton.setOnClickListener {
                Log.d("GettingStartedFragment", "Register button clicked")
                try {
                    findNavController().navigate(R.id.action_gettingStartedFragment_to_signUpFragment1)
                    Log.d("GettingStartedFragment", "Navigation to signup successful")
                } catch (e: Exception) {
                    Log.e("GettingStartedFragment", "Navigation to signup failed: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("GettingStartedFragment", "Register button not found!")
        }
    }
}