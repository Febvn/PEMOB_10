package com.example.myfirstkmpapp

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual fun getPlatformName(): String = "Java ${System.getProperty("java.version")}"