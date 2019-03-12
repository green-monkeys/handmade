package com.greenmonkeys.handmade.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CGA::class, Artisan::class, Group::class], version = 1, exportSchema = false)
@TypeConverters(PhoneTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun artisanDao(): ArtisanDao
    abstract fun cgaDao(): CGADao
    abstract fun groupDao(): GroupDao
}