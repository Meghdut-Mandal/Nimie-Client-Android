package com.meghdut.nimie.network

import com.google.protobuf.ByteString
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.data.model.LocalStatus
import com.meghdut.nimie.network.grpc_api.Nimie.*
import com.meghdut.nimie.network.grpc_api.NimieApiGrpc
import com.meghdut.nimie.ui.util.randomName
import io.grpc.ManagedChannelBuilder


object GrpcClient {

    private val connectionString = "2.tcp.ngrok.io:12488".trim()
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


    fun createUser(publicKey: ByteArray): Long {
        val user = stub.registerUser(
            RegisterUserRequest
                .newBuilder()
                .setPubicKey(ByteString.copyFrom(publicKey))
                .build()
        )

        println("User created !! ${user.userId} ${user.userId}")
        return user.userId
    }

    fun createStatus(
        status: String,
        userId: Long,
        publicKey: ByteArray,
        name: String
    ): LocalStatus {
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
            name,
            publicKey
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
                randomName,
                it.publicKey.toByteArray()
            )
        }
    }

    fun replyToStatus(
        reply: ByteArray,
        userId: Long,
        statusId: Long,
        otherName: String
    ): LocalConversation {
        val conversationCreated = stub.replyStatus(
            InitiateConversationRequest.newBuilder().setReply(ByteString.copyFrom(reply))
                .setStatusId(statusId)
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
            conversationCreated.publicKey.toByteArray()
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
                it.lastReply.toByteArray(),
                it.otherPublicKey.toByteArray()
            )
        }
    }

    fun sendInitialExchangeRequest(conversationId: Long, aesKey: ByteArray) {
        val initialExchangeKey = stub.initialExchangeKey(
            InitialKeyExchangeRequest.newBuilder().setAesKey(ByteString.copyFrom(aesKey))
                .setConversationId(conversationId).build()
        )

        println("Sent the Key Exchange Request ${initialExchangeKey.message}")
    }

    fun exchangeKeyRequest(conversationId: Long): ByteArray {
        val finalExchangeKeyResponse = stub.finalExchangeKey(
            FinalKeyExchangeRequest.newBuilder().setConversationId(conversationId).build()
        )

        return finalExchangeKeyResponse.aesKey.toByteArray()
    }

    fun startChatConversation(
        userId: Long,
        conversationId: Long,
        handler: (ChatMessage) -> Unit
    ): MessagingClient = GrpcMessageClientImpl(userId, channel, conversationId, handler)


}