package com.example.drivenext_antonova

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.drivenext_antonova.util.ConnectivityLiveData

class MainActivity : AppCompatActivity() {

    private lateinit var connectivityLiveData: ConnectivityLiveData
    private var isNavControllerReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //  наблюдение за сетью
        try {
            connectivityLiveData = ConnectivityLiveData(this)
            connectivityLiveData.observe(this) { connected ->
                handleNetworkChange(connected)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getNavController(): NavController? {
        return try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            navHostFragment?.navController
        } catch (e: Exception) {
            null
        }
    }

    private fun handleNetworkChange(connected: Boolean) {
        val controller = getNavController() ?: return

        val currentDestination = controller.currentDestination?.id

        if (!connected) {
            // "Нет соединения" только если его еще нет
            if (currentDestination != R.id.noConnectionFragment) {
                try {
                    // безопасную навигацию
                    controller.navigate(R.id.action_global_noConnectionFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            // Сеть появилась - закрываем экран
            if (currentDestination == R.id.noConnectionFragment) {
                try {
                    controller.popBackStack()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // наблюдение за жизненным циклом фрагментов
    override fun onResume() {
        super.onResume()
        // При возвращении в активность проверяет состояние сети
        connectivityLiveData.value?.let { connected ->
            handleNetworkChange(connected)
        }
    }
}