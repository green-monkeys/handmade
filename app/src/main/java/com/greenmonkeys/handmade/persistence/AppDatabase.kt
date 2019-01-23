package com.greenmonkeys.handmade.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CGA::class, Artisan::class], version = 1)
@TypeConverters(PhoneTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun artisanDao(): ArtisanDao
    abstract fun cgaDao(): CGADao
}