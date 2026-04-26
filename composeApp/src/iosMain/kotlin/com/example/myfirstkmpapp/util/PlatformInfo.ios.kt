package com.example.myfirstkmpapp.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import platform.UIKit.UIDevice

class IosDeviceInfo : DeviceInfo {
    override fun getModel(): String = UIDevice.currentDevice.model
    override fun getManufacturer(): String = "Apple"
    override fun getOSVersion(): String = UIDevice.currentDevice.systemVersion
    override fun getPlatform(): String = "iOS"
}

class IosNetworkMonitor : NetworkMonitor {
    override val isOnline: Flow<Boolean> = flowOf(true)
}

class IosBatteryInfo : BatteryInfo {
    override fun getBatteryLevel(): Int = (UIDevice.currentDevice.batteryLevel * 100).toInt()
    override fun isCharging(): Boolean = false
}

actual fun getDeviceInfo(context: Any?): DeviceInfo = IosDeviceInfo()
actual fun getNetworkMonitor(context: Any?): NetworkMonitor = IosNetworkMonitor()
actual fun getBatteryInfo(context: Any?): BatteryInfo = IosBatteryInfo()
