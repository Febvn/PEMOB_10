package com.example.myfirstkmpapp.repository

import com.example.myfirstkmpapp.data.Note
import com.example.myfirstkmpapp.database.AppDatabase
import com.example.myfirstkmpapp.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.withContext

class NoteRepository(private val database: AppDatabase) {
    private val queries = database.noteDbQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                println("DB FLOW: Emitting ${entities.size} notes from database")
                entities.map { it.toNote() }
            }
    }

    fun getNotesByQuery(queryText: String): Flow<List<Note>> {
        return queries.getNotesByQuery(queryText)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toNote() } }
    }

    suspend fun insertNote(note: Note) = withContext(Dispatchers.Default) {
        println("DB ACTION: Attempting to insert note: '${note.title}'")
        try {
            database.transaction {
                queries.insertNote(
                    title = note.title,
                    content = note.content,
                    color = note.color,
                    isFavorite = if (note.isFavorite) 1L else 0L,
                    timestamp = note.timestamp,
                    cloudId = null
                )
            }
            println("DB ACTION: Insert SUCCESS")
        } catch (e: Exception) {
            println("DB ACTION: Insert FAILED: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun updateNote(note: Note) = withContext(Dispatchers.Default) {
        database.transaction {
            queries.updateNote(
                id = note.id,
                title = note.title,
                content = note.content,
                color = note.color,
                isFavorite = if (note.isFavorite) 1L else 0L,
                timestamp = note.timestamp,
                cloudId = null
            )
        }
    }

    suspend fun deleteNote(id: Long) = withContext(Dispatchers.Default) {
        database.transaction {
            queries.deleteNoteById(id)
        }
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.Default) {
        queries.updateFavoriteStatus(if (isFavorite) 1L else 0L, id)
    }

    private fun NoteEntity.toNote(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            isFavorite = isFavorite == 1L,
            timestamp = timestamp,
            color = color
        )
    }
}
