package com.example.drivenext_antonova.ui.add_car

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddCarAddressFragment : Fragment(R.layout.fragment_add_car1) {

    private lateinit var etAddress: TextInputEditText
    private lateinit var btnNext: MaterialButton
    private lateinit var ivBack: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etAddress = view.findViewById(R.id.etAddress)
        btnNext = view.findViewById(R.id.btnNext)
        ivBack = view.findViewById(R.id.ivBack)

        setupAddressInputListener()
        setupButtonClick()
        setupBackButton()
    }

    private fun setupAddressInputListener() {
        etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val address = s?.toString()?.trim().orEmpty()
                updateNextButtonState(address)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateNextButtonState(address: String) {
        val isAddressValid = address.isNotEmpty()

        btnNext.isEnabled = isAddressValid
        btnNext.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                if (isAddressValid) R.color.primary_color else R.color.disabled_color
            )
        )
    }

    private fun setupButtonClick() {
        btnNext.setOnClickListener {
            val address = etAddress.text?.toString()?.trim().orEmpty()
            if (address.isNotEmpty()) {
                findNavController().navigate(R.id.action_addCarAddressFragment_to_addCarDescriptionFragment)

                Toast.makeText(requireContext(), "Адрес сохранён: $address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBackButton() {
        ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
