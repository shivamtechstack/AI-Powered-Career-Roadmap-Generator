package com.sycodes.careerbot.service

import com.sycodes.careerbot.data.GeminiModel.GeminiRequest
import com.sycodes.careerbot.data.GeminiModel.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}
