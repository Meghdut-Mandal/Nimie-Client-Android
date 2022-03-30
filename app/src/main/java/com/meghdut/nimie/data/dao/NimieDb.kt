package com.meghdut.nimie.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.meghdut.nimie.data.model.*


@Database(
    entities = [LocalUser::class,
        LocalConversation::class,
        ChatMessage::class,
        LocalStatus::class,
        ConversationKeyEntry::class], version = 5
)
abstract class NimieDb : RoomDatabase() {

    abstract fun userDao(): LocalUserDao

    abstract fun chatDao(): ChatDao

    abstract fun conversationDao(): ConversationDao

    abstract fun statusDao(): StatusDao

    companion object {

        private var db: NimieDb? = null

        fun create(context: Context): NimieDb {
            if (db == null) {
                db = Room.databaseBuilder(
                    context,
                    NimieDb::class.java,
                    "nimie_db"
                ).build()
            }
            return db!!
        }
    }
}