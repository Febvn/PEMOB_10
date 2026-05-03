package com.example.myfirstkmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstkmpapp.data.Note
import com.example.myfirstkmpapp.repository.NoteRepository
import com.example.myfirstkmpapp.currentTimeMillis
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.myfirstkmpapp.repository.SettingsRepository
import com.example.myfirstkmpapp.util.NetworkMonitor
import com.example.myfirstkmpapp.util.ShareManager
import com.example.myfirstkmpapp.data.remote.GeminiService

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val favorites: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val isAiLoading: Boolean = false,
    val aiError: String? = null
)


class NoteViewModel(
    private val repository: NoteRepository,
    private val settingsRepository: SettingsRepository,
    networkMonitor: NetworkMonitor,
    private val shareManager: ShareManager,
    private val geminiService: GeminiService
) : ViewModel() {
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val latency: StateFlow<Long> = networkMonitor.latency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    private val sortOrderFlow = settingsRepository.sortOrder
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

    fun shareNote(note: Note) {
        val text = "--- ${note.title} ---\n\n${note.content}"
        shareManager.shareText(text, "Share ${note.title}")
    }

    fun generateAiContent(prompt: String, onResponse: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiLoading = true, aiError = null) }
            val result = geminiService.generateResponse(prompt)
            result.onSuccess { text ->
                _uiState.update { it.copy(isAiLoading = false) }
                onResponse(text)
            }.onFailure { error ->
                _uiState.update { it.copy(isAiLoading = false, aiError = error.message) }
            }
        }
    }

    fun translateNote(note: Note, targetLanguage: String = "English") {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiLoading = true, aiError = null) }
            val prompt = "Translate the following note content to $targetLanguage. Keep the original meaning and tone: ${note.content}"
            val result = geminiService.generateResponse(prompt)
            result.onSuccess { translatedText ->
                _uiState.update { it.copy(isAiLoading = false) }
                repository.updateNote(note.copy(content = translatedText, timestamp = currentTimeMillis()))
            }.onFailure { error ->
                _uiState.update { it.copy(isAiLoading = false, aiError = error.message) }
            }
        }
    }
}
