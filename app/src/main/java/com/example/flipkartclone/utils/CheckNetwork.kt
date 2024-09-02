package com.example.flipkartclone.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import android.net.NetworkCapabilities

class CheckNetwork {

    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    fun hasInternetConnection(context: Context) : Boolean{
        val connectivityManager =
            ContextCompat.getSystemService(context, ConnectivityManager::class.java)

        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return  when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}