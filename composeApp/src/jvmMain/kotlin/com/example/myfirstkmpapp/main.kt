package com.example.myfirstkmpapp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val driverFactory = com.example.myfirstkmpapp.database.DriverFactory()
    val dataStore = com.example.myfirstkmpapp.util.createDataStore()
    val container = DependencyContainer(driverFactory, dataStore)

    Window(
        onCloseRequest = ::exitApplication,
        title = "MyFirstKMPApp (Mobile Preview)",
        state = rememberWindowState(width = 360.dp, height = 800.dp),
    ) {
        App(container)
    }
}