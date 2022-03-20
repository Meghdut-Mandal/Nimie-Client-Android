package com.meghdut.nimie.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "local_status")
data class LocalStatus(
    @PrimaryKey
    @SerializedName("status_id")
    val statusId:Long,
    @SerializedName("text")
    val text:String,
    @SerializedName("created_time")
    val createdTime :Long,
    @SerializedName("link_id")
    val linkId:String,

    @SerializedName("user_name")
    val userName :String,
    @SerializedName("avatar")
    val avatar:String
)
