package com.greenmonkeys.handmade.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["email"], unique = true)])
data class CGA(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val salt: String,
    val phone: String
) {
    override fun toString(): String {
        return "CGA(id=$id,email=$email)"
    }

    fun getFullName(): String {
        return "$firstName $lastName"
    }
}
