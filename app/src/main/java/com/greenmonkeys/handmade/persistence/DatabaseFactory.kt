package com.greenmonkeys.handmade.persistence

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseFactory {
    var db: RoomDatabase? = null

    fun getDatabase(applicationContext: Context): RoomDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "handmade"
            ).build()
        }
        return db!!
    }
}