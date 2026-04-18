package com.example.myfirstkmpapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myfirstkmpapp.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val favorites: List<Note> = emptyList()
)

class NoteViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        // Start with an empty note list as requested
        _uiState.value = NoteUiState(
            notes = emptyList(),
            favorites = emptyList()
        )
    }

    fun addNote(title: String, content: String) {
        val currentTime = (0..100000000).random().toLong() // Mocked for speed
        val newNote = Note(
            id = "note_${currentTime}_${(0..1000).random()}",
            title = title,
            content = content,
            timestamp = currentTime
        )
        val updatedNotes = _uiState.value.notes + newNote
        updateState(updatedNotes)
    }

    fun updateNote(updatedNote: Note) {
        val updatedNotes = _uiState.value.notes.map { if (it.id == updatedNote.id) updatedNote else it }
        updateState(updatedNotes)
    }

    fun deleteNote(id: String) {
        val updatedNotes = _uiState.value.notes.filter { it.id != id }
        updateState(updatedNotes)
    }

    fun toggleFavorite(id: String) {
        val updatedNotes = _uiState.value.notes.map { 
            if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it 
        }
        updateState(updatedNotes)
    }

    private fun updateState(notes: List<Note>) {
        _uiState.value = _uiState.value.copy(
            notes = notes,
            favorites = notes.filter { it.isFavorite }
        )
    }

    fun getNoteById(id: String?): Note? {
        return _uiState.value.notes.find { it.id == id }
    }
}
