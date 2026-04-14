package com.example.myfirstkmpapp.news.repository

import com.example.myfirstkmpapp.news.data.model.NewsArticle
import com.example.myfirstkmpapp.news.data.remote.NewsApiService

class NewsRepository(private val apiService: NewsApiService) {
    suspend fun getNewsArticles(): List<NewsArticle> {
        return apiService.fetchNews().results
    }
}
