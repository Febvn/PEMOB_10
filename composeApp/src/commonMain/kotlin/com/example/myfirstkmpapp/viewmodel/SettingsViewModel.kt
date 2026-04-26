package com.example.myfirstkmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstkmpapp.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.myfirstkmpapp.util.DeviceInfo
import com.example.myfirstkmpapp.util.BatteryInfo

class SettingsViewModel(
    private val repository: SettingsRepository,
    val deviceInfo: DeviceInfo,
    val batteryInfo: BatteryInfo
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = repository.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val sortOrder: StateFlow<String> = repository.sortOrder
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Newest")

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDarkMode(enabled)
        }
    }

    fun setSortOrder(order: String) {
        viewModelScope.launch {
            repository.setSortOrder(order)
        }
    }
}
