package com.example.drivenext_antonova.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton

class HomepageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton: MaterialButton = view.findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener {
            // Простой выход без PrefsManager
            performLogout()
        }
    }

    private fun performLogout() {
        // переход на стартовый экран
        try {
            findNavController().navigate(R.id.action_homepageFragment_to_gettingStartedFragment)
        } catch (e: Exception) {

            Toast.makeText(
                requireContext(),
                "Выход выполнен",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}