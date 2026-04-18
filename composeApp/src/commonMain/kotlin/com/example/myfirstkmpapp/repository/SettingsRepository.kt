package com.example.myfirstkmpapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    
    val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE] ?: true
    }

    val sortOrder: Flow<String> = dataStore.data.map { preferences ->
        preferences[SORT_ORDER] ?: "Newest"
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = enabled
        }
    }

    suspend fun setSortOrder(order: String) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER] = order
        }
    }

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }
}
