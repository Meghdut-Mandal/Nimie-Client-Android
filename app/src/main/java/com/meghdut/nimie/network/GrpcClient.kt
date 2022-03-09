package com.meghdut.nimie.network

import com.meghdut.nimie.network.grpc_api.Nimie
import com.meghdut.nimie.network.grpc_api.NimieApiGrpc
import io.grpc.ManagedChannelBuilder


object GrpcClient {
    private val channel by lazy {
        ManagedChannelBuilder
//            .forTarget("nimie.smartsms3456.tech")
            .forAddress("2.tcp.ngrok.io", 15225)
        .usePlaintext()
        .build()
    }
    private val stub by lazy { NimieApiGrpc.newBlockingStub(channel) }


    fun createUser(publicKey:String): Long {
        val user = stub.registerUser(
            Nimie.RegisterUserRequest
                .newBuilder()
                .setPubicKey(publicKey)
                .build()
        )

        println("User created !! ${user.jwt} ${user.userId}")
        return user.userId

    }
}