package com.meghdut.nimie.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.security.PublicKey

@Entity(tableName = "local_status")
data class LocalStatus(
    @PrimaryKey
    @SerializedName("status_id")
    val statusId: Long,
    @SerializedName("text")
    val text: String,
    @SerializedName("created_time")
    val createdTime: Long,
    @SerializedName("link_id")
    val linkId: String,

    @SerializedName("user_name")
    val userName: String,

    @SerializedName("public_key")
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val publicKey: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalStatus

        if (statusId != other.statusId) return false
        if (text != other.text) return false
        if (createdTime != other.createdTime) return false
        if (linkId != other.linkId) return false
        if (userName != other.userName) return false
        if (!publicKey.contentEquals(other.publicKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = statusId.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + createdTime.hashCode()
        result = 31 * result + linkId.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + publicKey.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "LocalStatus(statusId=$statusId, text='$text', createdTime=$createdTime, linkId='$linkId', userName='$userName', publicKey=${publicKey.contentHashCode()})"
    }


}
