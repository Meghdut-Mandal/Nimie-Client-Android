package com.meghdut.nimie.network

import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.network.grpc_api.Nimie
import com.meghdut.nimie.network.grpc_api.NimieApiGrpc
import io.grpc.ManagedChannelBuilder


object GrpcClient {

    val connectionString = "2.tcp.ngrok.io:13467".trim()
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
            Nimie.RegisterUserRequest
                .newBuilder()
                .setPubicKey(publicKey)
                .build()
        )

        println("User created !! ${user.jwt} ${user.userId}")
        return user.userId
    }

    fun createStatus(status: String, userId: Long): LocalStatus {
        val created = stub.createStatus(
            Nimie.CreateStatusRequest
                .newBuilder()
                .setText(status)
                .setUserId(userId)
                .build()
        )

        return LocalStatus(created.statusId,status, created.createTime,created.linkId)
    }
}