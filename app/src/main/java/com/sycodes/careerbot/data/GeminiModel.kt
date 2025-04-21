package com.sycodes.careerbot.data

class GeminiModel {
    data class Part(
        val text: String
    )

    data class Content(
        val parts: List<Part>
    )

    data class GeminiRequest(
        val contents: List<Content>
    )

    data class GeminiResponse(
        val candidates: List<GeminiCandidate>
    )

    data class GeminiCandidate(
        val content: GeminiContent
    )

    data class GeminiContent(
        val parts: List<Part>
    )
}