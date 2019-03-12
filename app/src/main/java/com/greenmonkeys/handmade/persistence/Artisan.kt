package com.greenmonkeys.handmade.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    foreignKeys = [ForeignKey(entity = CGA::class, parentColumns = arrayOf("email"), childColumns = arrayOf("cga_id"))],
    primaryKeys = ["email", "cga_id"],
    indices = [
        Index(value = ["email"]),
        Index(value = ["cga_id"]),
        Index(value = ["email", "cga_id"], unique = true)
    ]
)
data class Artisan(
    @ColumnInfo(name = "cga_id") val cgaId: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    val email: String,
    val password: String?,
    val salt: String?,
    val phone: String,
    @ColumnInfo(name = "phone_type") val phoneType: PhoneType,
    @ColumnInfo(name = "sms") val smsNotifications: Boolean,
    @ColumnInfo(name = "has_logged_in") val hasLoggedIn: Boolean
) {
    override fun toString(): String {
        return "Artisan(cga_id=$cgaId,email=$email)"
    }

    fun getFullName(): String {
        return "$firstName $lastName"
    }
}
