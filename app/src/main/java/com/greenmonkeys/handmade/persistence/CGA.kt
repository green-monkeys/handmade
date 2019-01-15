package com.greenmonkeys.handmade.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CGA (
    @PrimaryKey var id: Int,
    var username: String,
    var password: String,
    var salt: String,
    var phone: String
)
