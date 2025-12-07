package com.example.drivenext_antonova.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.example.drivenext_antonova.data.AuthRepository

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val PREFS_NAME = "app_prefs"

    private lateinit var prefs: SharedPreferences

    // UI
    private var imgAvatar: ImageView? = null
    private var tvName: TextView? = null
    private var tvEmail: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        imgAvatar = view.findViewById(R.id.imgAvatar)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)

        val itemProfile = view.findViewById<View>(R.id.itemProfile)
        val itemBookings = view.findViewById<View>(R.id.itemBookings)
        val itemAddCar = view.findViewById<View>(R.id.itemAddCar)
        val itemTheme = view.findViewById<View>(R.id.itemTheme)
        val itemNotifications = view.findViewById<View>(R.id.itemNotifications)
        val itemHelp = view.findViewById<View>(R.id.itemHelp)
        val itemInvite = view.findViewById<View>(R.id.itemInvite)

        refreshProfileUi()

        itemProfile.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
            } catch (e: Exception) { }
        }

        itemBookings.setOnClickListener {
            findNavController().navigate(R.id.homepageFragment)
        }

        // ✅ Переход на BecomeHostFragment
        itemAddCar.setOnClickListener {
            try {
                findNavController().navigate(R.id.becomeHostFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        itemTheme.setOnClickListener { /* placeholder */ }
        itemNotifications.setOnClickListener { /* placeholder */ }
        itemHelp.setOnClickListener { /* placeholder */ }
        itemInvite.setOnClickListener { /* placeholder */ }
    }



    override fun onResume() {
        super.onResume()
        refreshProfileUi()
    }

    private fun refreshProfileUi() {
        val user = AuthRepository.getCurrentUser()
        if (user == null) return

        val name = "${user.name} ${user.surname}".trim()
        tvName?.text = name
        tvEmail?.text = user.email

        try {
            val uri = user.profilePhoto
            if (uri != null) {
                imgAvatar?.setImageURI(uri)
            } else {
                imgAvatar?.setImageResource(R.drawable.ic_person)
            }
        } catch (e: Exception) {
            imgAvatar?.setImageResource(R.drawable.ic_person)
        }
    }

}