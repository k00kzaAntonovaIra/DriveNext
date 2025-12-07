package com.example.drivenext_antonova.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

/**
 * LiveData that monitors network connectivity status
 */
class ConnectivityLiveData(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager: ConnectivityManager? =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            // On some devices onLost may be called later, so check current state
            postValue(isCurrentlyConnected())
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            postValue(isCurrentlyConnected())
        }
    }

    override fun onActive() {
        super.onActive()
        postValue(isCurrentlyConnected())
        try {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager?.registerNetworkCallback(request, networkCallback)
        } catch (ex: Exception) {
            // Fallback: if registration fails, keep current value
        }
    }

    override fun onInactive() {
        super.onInactive()
        try {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        } catch (ex: Exception) {
            // Ignore unregistration errors
        }
    }

    /**
     * Checks if device is currently connected to internet
     */
    private fun isCurrentlyConnected(): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
    }
}