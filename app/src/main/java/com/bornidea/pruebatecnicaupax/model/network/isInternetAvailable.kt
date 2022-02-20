package com.bornidea.pruebatecnicaupax.model.network

import android.content.Context
import android.net.ConnectivityManager

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.activeNetworkInfo.also {
        return it != null && it.isConnected
    }
}