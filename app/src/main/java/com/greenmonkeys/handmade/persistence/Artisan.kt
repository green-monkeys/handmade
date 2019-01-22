package com.greenmonkeys.handmade.persistence

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(entity = CGA::class, parentColumns = arrayOf("id"), childColumns = arrayOf("cga_id"))],
    primaryKeys = ["email", "cga_id"],
    indices = [Index(value = ["email"], unique = true)]
)
data class Artisan(
    @ColumnInfo(name = "cga_id") val cgaId: Int,
    val email: String,
    val password: String,
    val salt: String,
    val phone: String,
    val phoneType: PhoneType,
    val smsNotifications: Boolean
) {
    override fun toString(): String {
        return "Artisan($cgaId,$email,$password,$salt,$phone,$phoneType,$smsNotifications)"
    }
}
