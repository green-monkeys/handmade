package com.greenmonkeys.handmade.persistence

import android.content.Context
import androidx.room.Room

object DatabaseFactory {
    var db: AppDatabase? = null

    fun getDatabase(applicationContext: Context): AppDatabase {
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