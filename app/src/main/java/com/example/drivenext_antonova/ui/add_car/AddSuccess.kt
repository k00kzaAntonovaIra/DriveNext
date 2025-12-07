package com.example.drivenext_antonova.ui.add_car

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton

class AddSuccessFragment : Fragment(R.layout.fragment_success_add) {

    private lateinit var ivBack: ImageButton
    private lateinit var btnNext: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBack = view.findViewById(R.id.ivBack)
        btnNext = view.findViewById(R.id.btnNext)

        ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_AddSuccessFragment_to_homepageFragment)
        }
    }
}
