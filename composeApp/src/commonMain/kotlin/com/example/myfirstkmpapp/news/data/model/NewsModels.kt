package com.example.myfirstkmpapp.news.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val results: List<NewsArticle>
)

@Serializable
data class NewsArticle(
    val id: Int,
    val title: String,
    val summary: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("news_site")
    val newsSite: String,
    @SerialName("published_at")
    val publishedAt: String,
    val url: String
)
