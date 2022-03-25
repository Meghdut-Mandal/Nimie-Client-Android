package com.meghdut.nimie.network

import com.meghdut.nimie.data.model.ChatMessage

interface MessagingClient {
    fun sendMessage(chatMessage: ChatMessage)
    fun closeChat()
}
