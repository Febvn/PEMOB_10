package com.example.myfirstkmpapp.news.repository

import com.example.myfirstkmpapp.news.data.model.NewsArticle
import com.example.myfirstkmpapp.news.data.model.OkSurfArticle
import com.example.myfirstkmpapp.news.data.remote.NewsApiService
import kotlinx.datetime.Clock

class NewsRepository(private val apiService: NewsApiService) {
    suspend fun getNewsArticles(query: String? = null, category: String? = "World"): List<NewsArticle> {
        return try {
            val response = apiService.fetchAllNews()
            
            // Pilih list berdasarkan kategori (Menambahkan lebih banyak genre)
            val rawList = when (category?.lowercase()) {
                "business" -> response.business
                "technology" -> response.technology
                "entertainment" -> response.entertainment
                "science" -> response.science
                "health" -> response.health
                "sports" -> response.sports
                "trending" -> response.world.shuffled().take(10) // Mock Trending dari World
                "lifestyle" -> response.entertainment.shuffled().take(10) // Mock Lifestyle
                else -> response.world
            }

            var result = rawList.map { it.toInternal() }

            if (!query.isNullOrEmpty()) {
                result = result.filter { 
                    it.title.contains(query, ignoreCase = true)
                }
            }

            // Jika masih kosong, coba tampilkan berita World sebagai default
            if (result.isEmpty() && query.isNullOrEmpty()) {
                result = response.world.map { it.toInternal() }
            }

            result
        } catch (e: Exception) {
            // Jika benar-benar gagal koneksi, sampaikan errornya
            error("Connection failed: ${e.message}")
        }
    }

    private fun OkSurfArticle.toInternal(): NewsArticle {
        return NewsArticle(
            title = this.title,
            description = "Click to read more from ${this.source}",
            url = this.link,
            imageUrl = this.og,
            publishedAt = Clock.System.now().toString(),
            source = this.source
        )
    }
}
