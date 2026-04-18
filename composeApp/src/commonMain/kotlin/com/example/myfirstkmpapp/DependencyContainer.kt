package com.example.myfirstkmpapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.myfirstkmpapp.database.AppDatabase
import com.example.myfirstkmpapp.database.DriverFactory
import com.example.myfirstkmpapp.database.createDatabase
import com.example.myfirstkmpapp.repository.NoteRepository
import com.example.myfirstkmpapp.repository.SettingsRepository

class DependencyContainer(
    driverFactory: DriverFactory,
    dataStore: DataStore<Preferences>
) {
    val database: AppDatabase = createDatabase(driverFactory)
    val noteRepository = NoteRepository(database)
    val settingsRepository = SettingsRepository(dataStore)
}
