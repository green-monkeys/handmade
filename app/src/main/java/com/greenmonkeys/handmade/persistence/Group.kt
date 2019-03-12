package com.greenmonkeys.handmade.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    foreignKeys = [
        ForeignKey(entity = Artisan::class, parentColumns = arrayOf("email", "cga_id"), childColumns = arrayOf("artisan_id", "cga_id"))
    ],
    primaryKeys = ["group_name", "cga_id", "artisan_id"],
    indices = [Index(value = ["artisan_id", "cga_id"])]
)
data class Group(
    @ColumnInfo(name = "group_name") val groupName: String,
    @ColumnInfo(name = "cga_id") val cgaId: String,
    @ColumnInfo(name = "artisan_id") val artisanId: String
) {
    override fun toString(): String {
        return "($groupName,$cgaId,$artisanId)"
    }
}