package com.example.drivenext_antonova.ui.add_car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.example.drivenext_antonova.R

class BecomeHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_become_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnStart: MaterialButton = view.findViewById(R.id.btnStart)
        val ivBack: ImageButton = view.findViewById(R.id.ivBack)

        btnStart.setOnClickListener {
            // Переход к AddCarAddressFragment
            findNavController().navigate(R.id.action_becomeHostFragment_to_addCarAddressFragment)
        }

        ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
