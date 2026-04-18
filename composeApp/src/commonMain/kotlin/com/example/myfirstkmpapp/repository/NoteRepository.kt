package com.example.myfirstkmpapp.repository

import com.example.myfirstkmpapp.data.Note
import com.example.myfirstkmpapp.database.AppDatabase
import com.example.myfirstkmpapp.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class NoteRepository(private val database: AppDatabase) {
    private val queries = database.noteDbQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toNote() } }
    }

    fun getNotesByQuery(queryText: String): Flow<List<Note>> {
        return queries.getNotesByQuery(queryText)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toNote() } }
    }

    fun getFavoriteNotes(): Flow<List<Note>> {
        return queries.getFavoriteNotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toNote() } }
    }

    suspend fun getNoteById(id: Long): Note? = withContext(Dispatchers.IO) {
        queries.getNoteById(id).executeAsOneOrNull()?.toNote()
    }

    suspend fun insertNote(note: Note) = withContext(Dispatchers.IO) {
        queries.insertNote(
            title = note.title,
            content = note.content,
            color = note.color,
            isFavorite = if (note.isFavorite) 1L else 0L,
            timestamp = note.timestamp,
            cloudId = null
        )
    }

    suspend fun updateNote(note: Note) = withContext(Dispatchers.IO) {
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

    suspend fun deleteNote(id: Long) = withContext(Dispatchers.IO) {
        queries.deleteNoteById(id)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
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
