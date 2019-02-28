package com.greenmonkeys.handmade.persistence

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["email"], unique = true)])
data class CGA(
    @PrimaryKey val email: String,
    val name: String,
    val ownsPhone: Boolean
) {
    override fun toString(): String {
        return "CGA $name ($email)"
    }
}
