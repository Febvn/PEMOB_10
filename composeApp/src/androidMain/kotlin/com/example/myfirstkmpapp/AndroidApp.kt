package com.example.myfirstkmpapp

import android.app.Application
import com.example.myfirstkmpapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidApp)
        }
    }
}
