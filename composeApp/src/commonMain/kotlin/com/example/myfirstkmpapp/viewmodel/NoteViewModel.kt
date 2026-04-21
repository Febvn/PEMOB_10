package com.example.myfirstkmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstkmpapp.data.Note
import com.example.myfirstkmpapp.repository.NoteRepository
import com.example.myfirstkmpapp.currentTimeMillis
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val favorites: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

class NoteViewModel(
    private val repository: NoteRepository,
    private val sortOrderFlow: Flow<String> = flowOf("Newest")
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(NoteUiState(isLoading = true))
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        observeNotes()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeNotes() {
        combine(_searchQuery, sortOrderFlow) { query, sort ->
            query to sort
        }.flatMapLatest { (query, sort) ->
            val baseFlow = if (query.isBlank()) {
                repository.getAllNotes()
            } else {
                repository.getNotesByQuery(query)
            }
            
            baseFlow.map { notes ->
                when (sort) {
                    "Oldest" -> notes.sortedBy { it.timestamp }
                    "A-Z" -> notes.sortedBy { it.title.lowercase() }
                    else -> notes.sortedByDescending { it.timestamp } // Newest
                }
            }
        }.onEach { notes ->
            _uiState.update { it.copy(
                notes = notes,
                favorites = notes.filter { n -> n.isFavorite },
                isLoading = false
            ) }
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addNote(title: String, content: String, color: Long = 0xFFFFFFFF) {
        println("VM: addNote called - title='$title'")
        viewModelScope.launch {
            repository.insertNote(
                Note(
                    title = title,
                    content = content,
                    color = color,
                    timestamp = currentTimeMillis()
                )
            )
            println("VM: addNote completed")
        }
    }

    fun updateNote(updatedNote: Note) {
        println("VM: updateNote called - id=${updatedNote.id}")
        viewModelScope.launch {
            repository.updateNote(updatedNote.copy(timestamp = currentTimeMillis()))
        }
    }

    fun deleteNote(id: Long) {
        println("VM: deleteNote called - id=$id")
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun toggleFavorite(id: Long, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !currentIsFavorite)
        }
    }

    fun getNoteById(id: Long): Note? {
        return _uiState.value.notes.find { it.id == id }
    }
}
