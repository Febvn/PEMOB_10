package com.example.myfirstkmpapp.data

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val timestamp: Long = 0L,
    val color: Long = 0xFFFFFFFF
)
