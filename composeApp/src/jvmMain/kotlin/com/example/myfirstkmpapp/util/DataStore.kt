package com.example.myfirstkmpapp.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = { "prefs.preferences_pb" }
)
