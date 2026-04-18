package com.example.myfirstkmpapp.news.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Model untuk API ok.surf (Public News API)
 */
@Serializable
data class OkSurfResponse(
    @SerialName("Business") val business: List<OkSurfArticle> = emptyList(),
    @SerialName("Technology") val technology: List<OkSurfArticle> = emptyList(),
    @SerialName("Entertainment") val entertainment: List<OkSurfArticle> = emptyList(),
    @SerialName("World") val world: List<OkSurfArticle> = emptyList(),
    @SerialName("Science") val science: List<OkSurfArticle> = emptyList(),
    @SerialName("Health") val health: List<OkSurfArticle> = emptyList(),
    @SerialName("Sports") val sports: List<OkSurfArticle> = emptyList()
)

@Serializable
data class OkSurfArticle(
    val title: String,
    val link: String,
    val og: String? = null,
    val source: String? = null,
    @SerialName("source_icon") val sourceIcon: String? = null
)

/**
 * Model Internal Aplikasi (Mapping)
 */
@Serializable
data class NewsArticle(
    val title: String,
    val description: String? = null,
    val url: String,
    val imageUrl: String? = null,
    val publishedAt: String,
    val source: String? = null
)
