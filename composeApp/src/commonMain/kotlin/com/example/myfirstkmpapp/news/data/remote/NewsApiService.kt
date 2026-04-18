package com.example.myfirstkmpapp.news.data.remote

import com.example.myfirstkmpapp.news.data.model.OkSurfResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

class NewsApiService {
    private val okSurfUrl = "https://ok.surf/api/v1/cors/news-feed"
    private val fallbackUrl = "https://api.spaceflightnewsapi.net/v4/articles/?limit=20"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000 // 15 detik timeout
            connectTimeoutMillis = 10000
        }
        defaultRequest {
            header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    suspend fun fetchAllNews(): OkSurfResponse {
        println("NewsAPI: Fetching all news from $okSurfUrl...")
        val response = client.get(okSurfUrl).body<OkSurfResponse>()
        println("NewsAPI: Fetch success!")
        return response
    }

    // Fallback jika ok.surf bermasalah
    suspend fun fetchFallbackNews(): String {
        return client.get(fallbackUrl).body()
    }
}
