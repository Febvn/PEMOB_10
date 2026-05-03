package com.example.myfirstkmpapp.data.remote

import com.example.myfirstkmpapp.util.Constants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GeminiService {
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
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 15000
        }
    }

    suspend fun generateResponse(prompt: String): Result<String> {
        return try {
            val requestBody = GeminiRequest(
                contents = listOf(
                    GeminiRequest.Content(
                        parts = listOf(
                            GeminiRequest.Content.Part(text = prompt)
                        )
                    )
                ),
                system_instruction = GeminiRequest.Content(
                    parts = listOf(
                        GeminiRequest.Content.Part(
                            text = "You are a helpful and creative Note Assistant. " +
                                   "Your task is to help users write, summarize, and improve their personal notes. " +
                                   "Keep the tone helpful, professional, and concise. " +
                                   "If the user asks to improve a note, fix grammar and make it more engaging. " +
                                   "If the user asks for ideas, provide creative and useful suggestions."
                        )
                    )
                )
            )

            val httpResponse = client.post(Constants.GEMINI_BASE_URL) {
                parameter("key", Constants.GEMINI_API_KEY)
                header("X-goog-api-key", Constants.GEMINI_API_KEY)
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            if (httpResponse.status.isSuccess()) {
                val response: GeminiResponse = httpResponse.body()
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (text != null) {
                    Result.success(text)
                } else {
                    Result.failure(Exception("AI did not provide a response. Try again."))
                }
            } else {
                val errorBody = httpResponse.bodyAsText()
                Result.failure(Exception("Gemini API Error (${httpResponse.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
