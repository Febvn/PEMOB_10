package com.example.myfirstkmpapp.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        // Pindahkan ke folder project biar gampang dikontrol
        val databaseFile = File(System.getProperty("user.dir"), "notes_local.db")
        val isNewDatabase = !databaseFile.exists()
        
        println("DB SETUP: Database file: ${databaseFile.absolutePath}")
        
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${databaseFile.absolutePath}")
        
        if (isNewDatabase) {
            println("DB SETUP: New database detected. Creating schema...")
            AppDatabase.Schema.create(driver)
        } else {
            println("DB SETUP: Existing database found.")
        }
        
        return driver
    }
}
