package com.meghdut.nimie.network

import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.network.grpc_api.Nimie.*
import com.meghdut.nimie.network.grpc_api.NimieApiGrpc
import com.meghdut.nimie.ui.util.avatar
import com.meghdut.nimie.ui.util.randomName
import io.grpc.ManagedChannelBuilder


object GrpcClient {

    val connectionString = "2.tcp.ngrok.io:10515".trim()
    val split = connectionString.split(":")
    val name = split[0]
    val port = split[1].toInt()
    private val channel by lazy {
        ManagedChannelBuilder
//            .forTarget("nimie.smartsms3456.tech")
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
            randomName,
            avatar(created.linkId)
        )
    }

    fun getBulkStatus(offset: Int, limit: Int): List<LocalStatus> {
        val apiStatus = stub.getBulkStatus(
            GetBulkStatusRequest
                .newBuilder().setOffset(offset).setLimit(limit).build()
        )
        return apiStatus.bulkStatusOrBuilderList.map {
            LocalStatus(it.statusId,it.text,it.createTime,it.linkId, randomName, avatar(it.linkId))
        }
    }
}