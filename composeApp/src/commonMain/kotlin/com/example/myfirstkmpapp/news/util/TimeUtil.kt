package com.example.myfirstkmpapp.news.util

fun getRelativeTime(isoString: String): String {
    // Karena library kotlinx-datetime bermasalah di beberapa sistem, 
    // kita gunakan cara manual yang lebih aman untuk sementara.
    return try {
        if (isoString.length >= 10) isoString.take(10) else "Recent"
    } catch (e: Exception) {
        "Recent"
    }
}
