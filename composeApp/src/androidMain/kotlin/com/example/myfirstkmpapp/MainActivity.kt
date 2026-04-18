package com.example.myfirstkmpapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val driverFactory = com.example.myfirstkmpapp.database.DriverFactory(this)
        val dataStore = com.example.myfirstkmpapp.util.createDataStore(this)
        val container = DependencyContainer(driverFactory, dataStore)

        setContent {
            App(container)
        }
    }
}