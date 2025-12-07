package com.example.drivenext_antonova.ui.common

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R

class SplashFragment : Fragment() {

    private val splashDelay: Long = 3000 // 2 секунды
    private val handler = Handler(Looper.getMainLooper())
    private var navigationRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationRunnable = Runnable {
            navigateToNextScreen()
        }
        handler.postDelayed(navigationRunnable!!, splashDelay)
    }

    private fun navigateToNextScreen() {
        if (!isAdded || isDetached || isRemoving) {
            Log.w("SplashFragment", "Fragment is not in valid state for navigation")
            return
        }

        try {
            val navController = findNavController()
            // Всегда показываем onboarding при запуске приложения
            navController.navigate(R.id.action_splashFragment_to_onboardingFragment1)
        } catch (e: Exception) {
            Log.e("SplashFragment", "Navigation error: ${e.message}", e)
            // Fallback на onboarding при ошибке
            try {
                if (isAdded && !isDetached && !isRemoving) {
                    findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment1)
                }
            } catch (fallbackException: Exception) {
                Log.e("SplashFragment", "Fallback navigation failed: ${fallbackException.message}", fallbackException)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Очищаем Handler для предотвращения утечек памяти
        navigationRunnable?.let { handler.removeCallbacks(it) }
        navigationRunnable = null
    }
}