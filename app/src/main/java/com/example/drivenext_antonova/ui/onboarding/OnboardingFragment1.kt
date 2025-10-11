package com.example.drivenext_antonova.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton

class OnboardingFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding_1, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.buttonNext)
            .setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_onboardingFragment1_to_onboardingFragment2)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        view.findViewById<TextView>(R.id.textSkip)
            .setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_onboardingFragment1_to_gettingStartedFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }
}