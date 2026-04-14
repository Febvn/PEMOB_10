package com.example.myfirstkmpapp.news.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstkmpapp.news.data.model.NewsArticle
import com.example.myfirstkmpapp.news.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<NewsArticle>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val articles = repository.getNewsArticles()
                _uiState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
