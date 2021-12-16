package com.meghdut.nimie.model

import com.google.gson.annotations.SerializedName

data class StatusCreated(@SerializedName("unique_id") val uniqueId: Long)

data class UserCreated(
    @SerializedName("message") val message: String,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("user_id") val userId: Long
)

data class ConversationCreated(
    @SerializedName("conversation_id") val conversationId: Long,
    @SerializedName("public_key") val publicKey: String
)

data class ConversationMessagesResponse(@SerializedName("messages") val messages: List<ChatMessage>)

data class GetStatus(
    @SerializedName("text") val text: String,
    @SerializedName("status_id") val statusId: Long
)