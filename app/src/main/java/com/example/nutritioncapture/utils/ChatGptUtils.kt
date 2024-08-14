package com.example.nutritioncapture.utils

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.nutritioncapture.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import java.util.concurrent.Executor
import kotlin.time.Duration.Companion.seconds

class ChatGptUtils {
    private val TAG = ChatGptUtils::class.simpleName

    val openAi = OpenAI(
        token = BuildConfig.OPENAI_API_KEY,
        timeout = Timeout(socket = 90.seconds)
    )

    suspend fun getChatGptChatResponse(systemPrompt: String, userMessage: String) : String {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = systemPrompt
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = userMessage
                )
            )
        )

        val response = parseChatGptCompletionChunk(chatCompletionRequest)

        Log.d(TAG, "Chat-Gpt Response: $response")

        if(response != null) {
            return response
        }
        else {
            return "Chat-Gptのレスポンス: null"
        }

    }

    suspend fun parseChatGptCompletionChunk(chatCompletionRequest: ChatCompletionRequest) : String? {

        var resultResponse = ""

        try {
            val result = openAi.chatCompletion(chatCompletionRequest).choices
            resultResponse = result.get(0).message.content!!
        }
        catch (ex: Exception) {
            Log.d(TAG, "Error: $ex")
            resultResponse = "Error: $ex"
        }

        return resultResponse
    }
}