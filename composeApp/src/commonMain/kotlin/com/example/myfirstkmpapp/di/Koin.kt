package com.example.myfirstkmpapp.di

import com.example.myfirstkmpapp.database.createDatabase
import com.example.myfirstkmpapp.repository.NoteRepository
import com.example.myfirstkmpapp.repository.SettingsRepository
import com.example.myfirstkmpapp.viewmodel.NoteViewModel
import com.example.myfirstkmpapp.viewmodel.ProfileViewModel
import com.example.myfirstkmpapp.viewmodel.SettingsViewModel
import com.example.myfirstkmpapp.data.remote.GeminiService
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.factoryOf

/**
 * platformModule berisi dependensi yang spesifik untuk setiap platform.
 */
expect val platformModule: Module

/**
 * dataModule berisi dependensi untuk database dan repository.
 */
val dataModule = module {
    single { createDatabase(get()) }
    single { NoteRepository(get()) }
    single { SettingsRepository(get()) }
    single { GeminiService() }
}

/**
 * viewModelModule berisi dependensi untuk ViewModel.
 */
val viewModelModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::NoteViewModel)
    viewModelOf(::ProfileViewModel)
}

/**
 * commonModule menggabungkan dataModule dan viewModelModule.
 */
val commonModule = module {
    includes(dataModule, viewModelModule)
}

/**
 * Fungsi inisialisasi Koin untuk digunakan oleh platform-specific Application classes.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(platformModule, commonModule)
    }
