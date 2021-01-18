package com.wattpad.android.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.lang.ref.WeakReference

/**
 * This class is for checking user network state
 */

object NetworkUtil {

    fun isConnectedToNetwork(context: Context): Boolean {
        val weakReference  = WeakReference(context)

        val result: Boolean
        val connectivityManager =
            weakReference.get()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}