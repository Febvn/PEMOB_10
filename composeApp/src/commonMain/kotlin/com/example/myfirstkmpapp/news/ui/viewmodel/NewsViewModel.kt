package com.example.myfirstkmpapp.news.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstkmpapp.news.data.model.NewsArticle
import com.example.myfirstkmpapp.news.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategory = MutableStateFlow("World")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _favoriteArticles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val favoriteArticles: StateFlow<List<NewsArticle>> = _favoriteArticles

    private var searchJob: Job? = null

    val categories = listOf(
        "World", "Business", "Technology", "Trending", "Entertainment", "Lifestyle", "Science", "Health", "Sports"
    )

    private var itemsToShow = 2

    init {
        loadNews()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        itemsToShow = 2
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            loadNews()
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        _searchQuery.value = ""
        itemsToShow = 2
        loadNews()
    }

    fun toggleFavorite(article: NewsArticle) {
        val currentFavorites = _favoriteArticles.value.toMutableList()
        val existing = currentFavorites.find { it.url == article.url }
        if (existing != null) {
            currentFavorites.remove(existing)
        } else {
            currentFavorites.add(article)
        }
        _favoriteArticles.value = currentFavorites
    }

    fun isFavorite(article: NewsArticle): Boolean {
        return _favoriteArticles.value.any { it.url == article.url }
    }

    private var isLoadingMore = false
    private var cachedArticles: List<NewsArticle> = emptyList()

    private var loadJob: Job? = null

    fun loadNews(isLoadMore: Boolean = false, isPullRefresh: Boolean = false) {
        if (!isPullRefresh && isLoadingMore) return
        isLoadingMore = true
        
        if (isPullRefresh) {
            loadJob?.cancel()
        }
        
        loadJob = viewModelScope.launch {
            if (!isLoadMore && !isPullRefresh) {
                _uiState.value = NewsUiState.Loading
                itemsToShow = 2
            }
            
            if (isLoadMore) {
                itemsToShow += 8
            } else if (isPullRefresh) {
                itemsToShow = 2
            }
            
            try {
                // Force a 0.5 sec delay on pull-to-refresh so the user sees the spinner visually
                if (isPullRefresh) {
                    delay(500)
                }
                
                // Fetch dari network jika data kosong, filter ganti, atau DIPAKSA REFRESH
                if (cachedArticles.isEmpty() || isPullRefresh || (!isLoadMore && !isPullRefresh)) {
                    val query = _searchQuery.value.takeIf { it.isNotEmpty() }
                    val category = _selectedCategory.value
                    var articles = repository.getNewsArticles(query, category)
                    
                    if (isPullRefresh) {
                        articles = articles.shuffled() // Tetap acak untuk variasi sesudah download ulang
                    }
                    cachedArticles = articles
                }
                
                val listSize = cachedArticles.size
                val infiniteList = if (listSize > 0) {
                    List(itemsToShow) { index -> cachedArticles[index % listSize] }
                } else {
                    emptyList()
                }
                _uiState.value = NewsUiState.Success(infiniteList)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Unknown error occurred")
            } finally {
                isLoadingMore = false
            }
        }
    }
}
