package com.meghdut.nimie.network

import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.network.grpc_api.Nimie
import com.meghdut.nimie.network.grpc_api.NimieApiGrpc
import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver

class GrpcMessageClientImpl(
    val userId: Long,
    channel: ManagedChannel,
    val conversationId: Long,
    val handler: (ChatMessage) -> Unit
) : MessagingClient, StreamObserver<Nimie.ChatServerResponse> {

    private val conectedStub: NimieApiGrpc.NimieApiStub = NimieApiGrpc.newStub(channel)
    private val observer: StreamObserver<Nimie.ChatClientRequest> = conectedStub.chatConnect(this)


    override fun sendMessage(chatMessage: ChatMessage) {

        val apiMessage = Nimie.ApiTextMessage.newBuilder()
            .setMessage(chatMessage.message)
            .setConversationId(chatMessage.conversationId)
            .setCreateTime(0)
            .setIsSeen(false)
            .setUserId(userId)
            .setContentType(chatMessage.contentType)


        observer.onNext(
            Nimie.ChatClientRequest.newBuilder()
                .setMessage(apiMessage)
                .setMessageType(1)
                .build()
        )
    }

    override fun closeChat() {
        observer.onCompleted()
    }

    override fun onNext(response: Nimie.ChatServerResponse) {
        if (response.messageType == 1){
            val apiMessage = response.messages
            val chatMessage = apiMessage.let {
                ChatMessage(it.conversationId,
                    it.createTime,
                    it.message,
                    it.isSeen,
                    it.contentType,
                    it.messageId,
                    it.userId
                )
            }
            handler(chatMessage)
        }
    }

    override fun onError(t: Throwable) {
        t.printStackTrace()
     }

    override fun onCompleted() {
        println("Connection closed!")
    }
}