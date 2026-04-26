package com.example.myfirstkmpapp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.myfirstkmpapp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "MyFirstKMPApp (Mobile Preview)",
            state = rememberWindowState(width = 360.dp, height = 800.dp),
        ) {
            App()
        }
    }
}