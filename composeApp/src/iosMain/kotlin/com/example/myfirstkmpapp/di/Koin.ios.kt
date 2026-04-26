package com.example.myfirstkmpapp.di

import com.example.myfirstkmpapp.database.DriverFactory
import com.example.myfirstkmpapp.util.*
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DriverFactory() }
    single { createDataStore() }
    single { getDeviceInfo() }
    single { getNetworkMonitor() }
    single { getBatteryInfo() }
    single { getShareManager() }
}
