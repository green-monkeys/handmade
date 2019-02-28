package com.greenmonkeys.handmade.persistence

import androidx.room.TypeConverter

enum class PhoneType {
    NONE {
        override fun toString() = "None"
    }, SMART {
        override fun toString() = "Smart"
    }, DUMB {
        override fun toString() = "Basic"
    }
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
