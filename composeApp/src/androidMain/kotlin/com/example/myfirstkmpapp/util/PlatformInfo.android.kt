package com.example.myfirstkmpapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.BatteryManager
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class AndroidDeviceInfo(private val context: Context) : DeviceInfo {
    override fun getModel(): String = Build.MODEL
    override fun getManufacturer(): String = Build.MANUFACTURER
    override fun getOSVersion(): String = Build.VERSION.RELEASE
    override fun getPlatform(): String = "Android"
}

class AndroidNetworkMonitor(private val context: Context) : NetworkMonitor {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isOnline: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        
        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.onStart {
        emit(checkCurrentNetwork())
    }.distinctUntilChanged()

    override val latency: Flow<Long> = flow {
        while (true) {
            val start = System.currentTimeMillis()
            val reachable = try {
                java.net.InetAddress.getByName("8.8.8.8").isReachable(2000)
            } catch (e: Exception) {
                false
            }
            if (reachable) {
                emit(System.currentTimeMillis() - start)
            } else {
                emit(-1L)
            }
            delay(5000)
        }
    }

    private fun checkCurrentNetwork(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

class AndroidBatteryInfo(private val context: Context) : BatteryInfo {
    private val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    override fun getBatteryLevel(): Int {
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    override fun isCharging(): Boolean {
        return batteryManager.isCharging
    }
}

actual fun getDeviceInfo(context: Any?): DeviceInfo = AndroidDeviceInfo(context as Context)
actual fun getNetworkMonitor(context: Any?): NetworkMonitor = AndroidNetworkMonitor(context as Context)
actual fun getBatteryInfo(context: Any?): BatteryInfo = AndroidBatteryInfo(context as Context)
