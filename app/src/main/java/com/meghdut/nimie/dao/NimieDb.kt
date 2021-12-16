package com.meghdut.nimie.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.meghdut.nimie.model.LocalUser


@Database(entities = [LocalUser::class], version = 1)
abstract class NimieDb : RoomDatabase() {

    abstract fun userDao(): LocalUserDao

    companion object {
        fun create(context: Context): NimieDb = Room.databaseBuilder(
            context,
            NimieDb::class.java,
            "nimie_db"
        ).build()
    }
}