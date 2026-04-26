package com.example.myfirstkmpapp.util

import kotlinx.coroutines.flow.*

class JvmDeviceInfo : DeviceInfo {
    override fun getModel(): String = System.getProperty("os.arch") ?: "Unknown"
    override fun getManufacturer(): String = System.getProperty("os.name") ?: "Unknown"
    override fun getOSVersion(): String = System.getProperty("os.version") ?: "Unknown"
    override fun getPlatform(): String = "Desktop (JVM)"
}

class JvmNetworkMonitor : NetworkMonitor {
    private val _latency = kotlinx.coroutines.flow.MutableStateFlow(0L)
    override val latency: Flow<Long> = _latency.asStateFlow()

    override val isOnline: Flow<Boolean> = kotlinx.coroutines.flow.callbackFlow {
        val checkConnection = {
            val start = System.currentTimeMillis()
            try {
                val address = java.net.InetAddress.getByName("8.8.8.8")
                val reachable = address.isReachable(2000)
                if (reachable) {
                    _latency.value = System.currentTimeMillis() - start
                } else {
                    _latency.value = -1L
                }
                reachable
            } catch (e: Exception) {
                _latency.value = -1L
                false
            }
        }

        while (!isClosedForSend) {
            trySend(checkConnection())
            kotlinx.coroutines.delay(5000)
        }
    }.distinctUntilChanged()
}

class JvmBatteryInfo : BatteryInfo {
    override fun getBatteryLevel(): Int = 100
    override fun isCharging(): Boolean = true
}

actual fun getDeviceInfo(context: Any?): DeviceInfo = JvmDeviceInfo()
actual fun getNetworkMonitor(context: Any?): NetworkMonitor = JvmNetworkMonitor()
actual fun getBatteryInfo(context: Any?): BatteryInfo = JvmBatteryInfo()
