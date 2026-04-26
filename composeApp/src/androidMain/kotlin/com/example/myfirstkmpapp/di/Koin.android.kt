package com.example.myfirstkmpapp.di

import com.example.myfirstkmpapp.database.DriverFactory
import com.example.myfirstkmpapp.util.DeviceInfo
import com.example.myfirstkmpapp.util.NetworkMonitor
import com.example.myfirstkmpapp.util.BatteryInfo
import com.example.myfirstkmpapp.util.getDeviceInfo
import com.example.myfirstkmpapp.util.getNetworkMonitor
import com.example.myfirstkmpapp.util.getBatteryInfo
import com.example.myfirstkmpapp.util.getShareManager
import com.example.myfirstkmpapp.util.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

actual val platformModule: Module = module {
    single { DriverFactory(androidContext()) }
    single { createDataStore(androidContext()) }
    single { getDeviceInfo(androidContext()) }
    single { getNetworkMonitor(androidContext()) }
    single { getBatteryInfo(androidContext()) }
    single { getShareManager(androidContext()) }
}
