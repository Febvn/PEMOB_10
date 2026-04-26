package com.example.myfirstkmpapp.util

interface ShareManager {
    fun shareText(text: String, title: String = "Share Note")
}

expect fun getShareManager(context: Any? = null): ShareManager
