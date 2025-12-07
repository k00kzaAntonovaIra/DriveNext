package com.example.drivenext_antonova.util

import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Extension functions for UI components
 */

/**
 * Shows error message in TextInputLayout
 */
fun TextInputLayout.showError(message: String?) {
    this.error = message
}

/**
 * Clears error message in TextInputLayout
 */
fun TextInputLayout.clearError() {
    this.error = null
}

/**
 * Gets text from TextInputEditText or empty string if null
 */
fun TextInputEditText.getTextOrEmpty(): String = this.text?.toString().orEmpty()

/**
 * Gets text from EditText or empty string if null
 */
fun EditText.getTextOrEmpty(): String = this.text?.toString().orEmpty()

/**
 * Sets text to EditText safely
 */
fun EditText.setTextSafely(text: String?) {
    this.setText(text ?: "")
}

/**
 * Sets text to TextInputEditText safely
 */
fun TextInputEditText.setTextSafely(text: String?) {
    this.setText(text ?: "")
}

/**
 * Shows or hides view based on boolean
 */
fun View.show(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

/**
 * Makes view visible
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * Hides view
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * Makes view invisible (but keeps space)
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}