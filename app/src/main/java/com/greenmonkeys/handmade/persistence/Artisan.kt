package com.greenmonkeys.handmade.persistence

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(entity = CGA::class, parentColumns = arrayOf("id"), childColumns = arrayOf("cga_id"))],
    primaryKeys = ["id", "cga_id"]
)
data class Artisan(
    var id: Int,
    @ColumnInfo(name = "cga_id") var cgaId: Int,
    var username: String,
    var password: String,
    var salt: String,
    var phone: String,
    var phoneType: PhoneType,
    var smsNotifications: Boolean
)
