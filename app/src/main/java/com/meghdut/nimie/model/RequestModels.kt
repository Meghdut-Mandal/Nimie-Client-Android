package com.meghdut.nimie.model

import com.google.gson.annotations.SerializedName

data class RegisterUser(@SerializedName("public_key") val publicKey: String)
data class CreateStatus(@SerializedName("text") val text: String)
data class InitiateConversation(
    @SerializedName("reply") val reply: String,
    @SerializedName("status_id") val statusID: Long
)

data class GetConversationMessages(
    @SerializedName("message_id") val mesageId: Long,
    @SerializedName("conversation_id") val conversationId: Long
)