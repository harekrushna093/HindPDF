package com.example.data.firebase

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkMonitor(context: Context) {
  private val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

  val isOnlineFlow: Flow<Boolean> = callbackFlow {
    if (connectivityManager == null) {
      trySend(true)
      close()
      return@callbackFlow
    }

    val callback = object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        trySend(true)
      }

      override fun onLost(network: Network) {
        trySend(false)
      }

      override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
      ) {
        val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        trySend(hasInternet)
      }
    }

    // Send initial status
    val currentNetwork = connectivityManager.activeNetwork
    val caps = currentNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
    val initialOnline = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    trySend(initialOnline)

    val request = NetworkRequest.Builder()
      .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
      .build()

    try {
      connectivityManager.registerNetworkCallback(request, callback)
    } catch (e: Exception) {
      trySend(true)
    }

    awaitClose {
      try {
        connectivityManager.unregisterNetworkCallback(callback)
      } catch (_: Exception) {}
    }
  }.distinctUntilChanged()
}
