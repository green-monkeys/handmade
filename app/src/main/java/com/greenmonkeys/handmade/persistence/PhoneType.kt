package com.greenmonkeys.handmade.persistence

import androidx.room.TypeConverter

enum class PhoneType {
    NONE, SMART, DUMB
}

class PhoneTypeConverters {
    @TypeConverter
    fun fromPhoneType(pt: PhoneType?): String? {
        return when(pt) {
            PhoneType.NONE -> "NONE"
            PhoneType.DUMB -> "DUMB"
            PhoneType.SMART -> "SMART"
            else -> null
        }
    }

    @TypeConverter
    fun toPhoneType(pt: String?): PhoneType? {
        return when(pt) {
            "NONE" -> PhoneType.NONE
            "DUMB" -> PhoneType.DUMB
            "SMART" -> PhoneType.SMART
            else -> null
        }
    }
}
