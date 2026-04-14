package com.example.myfirstkmpapp.news.data.remote

import com.example.myfirstkmpapp.news.data.model.NewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NewsApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    suspend fun fetchNews(): NewsResponse {
        return client.get("https://api.spaceflightnewsapi.net/v4/articles/").body()
    }
}
