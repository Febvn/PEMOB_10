package com.example.myfirstkmpapp.util

import kotlinx.coroutines.flow.Flow

/**
 * DeviceInfo menyediakan informasi identitas perangkat.
 */
interface DeviceInfo {
    fun getModel(): String
    fun getManufacturer(): String
    fun getOSVersion(): String
    fun getPlatform(): String
}

/**
 * NetworkMonitor memantau status koneksi internet secara reaktif.
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
    val latency: Flow<Long> // Ping in ms
}

/**
 * BatteryInfo memberikan informasi status daya perangkat.
 */
interface BatteryInfo {
    fun getBatteryLevel(): Int
    fun isCharging(): Boolean
}

/**
 * Mendapatkan instance DeviceInfo sesuai platform (Android/JVM/iOS).
 */
expect fun getDeviceInfo(context: Any? = null): DeviceInfo

/**
 * Mendapatkan instance NetworkMonitor sesuai platform (Android/JVM/iOS).
 */
expect fun getNetworkMonitor(context: Any? = null): NetworkMonitor

/**
 * Mendapatkan instance BatteryInfo sesuai platform (Android/JVM/iOS).
 */
expect fun getBatteryInfo(context: Any? = null): BatteryInfo
