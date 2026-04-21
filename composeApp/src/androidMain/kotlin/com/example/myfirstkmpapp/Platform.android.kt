package com.example.myfirstkmpapp

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual fun getPlatformName(): String {
    return "Android ${android.os.Build.VERSION.SDK_INT}"
}