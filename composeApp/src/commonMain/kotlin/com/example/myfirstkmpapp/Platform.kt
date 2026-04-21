package com.example.myfirstkmpapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun currentTimeMillis(): Long

expect fun getPlatformName(): String

fun greet(): String {
    return "Hello from ${getPlatformName()}!"
}