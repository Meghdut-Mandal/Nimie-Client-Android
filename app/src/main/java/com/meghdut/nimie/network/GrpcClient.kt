package com.meghdut.nimie.network

import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.data.model.LocalStatus
import com.meghdut.nimie.network.grpc_api.Nimie.*
import com.meghdut.nimie.network.grpc_api.NimieApiGrpc
import com.meghdut.nimie.ui.util.randomName
import io.grpc.ManagedChannelBuilder


object GrpcClient {

    private val connectionString = "8.tcp.ngrok.io:14220".trim()
    private val split = connectionString.split(":")
    val name = split[0]
    private val port = split[1].toInt()
    private val channel by lazy {
        ManagedChannelBuilder
            .forAddress(name, port)
            .usePlaintext()
            .build()
    }
    private val stub by lazy { NimieApiGrpc.newBlockingStub(channel) }


    fun createUser(publicKey: String): Long {
        val user = stub.registerUser(
            RegisterUserRequest
                .newBuilder()
                .setPubicKey(publicKey)
                .build()
        )

        println("User created !! ${user.jwt} ${user.userId}")
        return user.userId
    }

    fun createStatus(status: String, userId: Long): LocalStatus {
        val created = stub.createStatus(
            CreateStatusRequest
                .newBuilder()
                .setText(status)
                .setUserId(userId)
                .build()
        )

        return LocalStatus(
            created.statusId,
            status,
            created.createTime,
            created.linkId,
            randomName
        )
    }

    fun getBulkStatus(offset: Int, limit: Int): List<LocalStatus> {
        val apiStatus = stub.getBulkStatus(
            GetBulkStatusRequest
                .newBuilder().setOffset(offset).setLimit(limit).build()
        )
        return apiStatus.bulkStatusOrBuilderList.map {
            LocalStatus(
                it.statusId,
                it.text,
                it.createTime,
                it.linkId,
                randomName
            )
        }
    }

    fun replyToStatus(
        reply: String,
        userId: Long,
        statusId: Long,
        otherName: String
    ): LocalConversation {
        val conversationCreated = stub.replyStatus(
            InitiateConversationRequest.newBuilder().setReply(reply).setStatusId(statusId)
                .setUserId(userId).build()
        )
        val timeNow = System.currentTimeMillis()

        return LocalConversation(
            conversationCreated.conversationId,
            statusId,
            timeNow,
            otherName,
            timeNow,
            reply,
            conversationCreated.publicKey
        )
    }

    fun getConversationList(userId: Long): List<LocalConversation> {
        val conversationList = stub.getConversationList(
            ConversationListRequest.newBuilder().setUserId(userId).setOffset(0).setLimit(100)
                .build()
        )

        return conversationList.conversationsOrBuilderList.map {
            LocalConversation(
                it.conversationId,
                it.statusId,
                it.createTime,
                randomName,
                it.createTime,
                it.lastReply,
                it.otherPublicKey
            )
        }
    }


    fun startChatConversation(
        userId: Long,
        conversationId: Long,
        handler: (ChatMessage) -> Unit
    ): MessagingClient = GrpcMessageClientImpl(userId, channel, conversationId,handler)

}