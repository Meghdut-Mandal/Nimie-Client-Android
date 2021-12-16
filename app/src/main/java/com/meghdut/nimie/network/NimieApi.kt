package com.meghdut.nimie.network

import com.meghdut.nimie.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NimieApi {


    @POST("/user/register")
    fun registerUser(@Body registerUser: RegisterUser): Call<UserCreated>

    @POST("/status/create")
    fun createStatus(@Body createStatus: CreateStatus): Call<StatusCreated>

    @GET("/status/{link_id}")
    fun getStatus(@Path("link_id") linkid: String): Call<GetStatus>

    @DELETE("/status/{status_id}")
    fun deleteStatus(@Path("status_id") statusId: Long)

    @POST("/status/reply")
    fun replyToStatus(@Body initiateConversation: InitiateConversation): Call<ConversationCreated>

    @POST("/conversation")
    fun readMessages(@Body getConversationMessages: GetConversationMessages): Call<ConversationMessagesResponse>


    companion object {

        val instance: NimieApi by lazy { create() }
        const val baseDomain = "25c3-114-29-226-213.ngrok.io"

        private fun create(): NimieApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://$baseDomain")
                .build()

            return retrofit.create(NimieApi::class.java)
        }

    }
}