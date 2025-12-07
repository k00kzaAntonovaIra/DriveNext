package com.example.drivenext_antonova.ui.add_car

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.AutoCompleteTextView

class AddCarDescriptionFragment : Fragment(R.layout.fragment_add_car2) {

    private lateinit var etYear: TextInputEditText
    private lateinit var etMark: TextInputEditText
    private lateinit var etModel: TextInputEditText
    private lateinit var etProbeg: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var actTransmission: AutoCompleteTextView
    private lateinit var btnSend: MaterialButton
    private lateinit var ivBack: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etYear = view.findViewById(R.id.etYear)
        etMark = view.findViewById(R.id.etMark)
        etModel = view.findViewById(R.id.etModel)
        etProbeg = view.findViewById(R.id.etProbeg)
        etDescription = view.findViewById(R.id.etDescription)
        actTransmission = view.findViewById(R.id.actTransmission)
        btnSend = view.findViewById(R.id.btnSend)
        ivBack = view.findViewById(R.id.ivBack)

        setupTransmissionDropdown()
        setupTextWatchers()
        setupButtons()
    }

    private fun setupTransmissionDropdown() {
        val transmissionOptions = listOf("A/T", "M/T")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, transmissionOptions)
        actTransmission.setAdapter(adapter)
        actTransmission.threshold = 1
    }

    private fun setupTextWatchers() {
        val textFields = listOf(etYear, etMark, etModel, actTransmission, etProbeg, etDescription)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSendButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        textFields.forEach { it.addTextChangedListener(watcher) }
    }

    private fun updateSendButtonState() {
        val allFilled = etYear.text?.isNotBlank() == true &&
                etMark.text?.isNotBlank() == true &&
                etModel.text?.isNotBlank() == true &&
                actTransmission.text?.isNotBlank() == true &&
                etProbeg.text?.isNotBlank() == true &&
                etDescription.text?.isNotBlank() == true

        btnSend.isEnabled = allFilled
        btnSend.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                if (allFilled) R.color.primary_color else R.color.disabled_color
            )
        )
    }

    private fun setupButtons() {
        ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnSend.setOnClickListener {
            if (btnSend.isEnabled) {
                Toast.makeText(requireContext(), "Данные автомобиля отправлены!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addCarDescriptionFragment_to_addCarPhotosFragment)
            }
        }
    }
}
