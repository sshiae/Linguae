package com.example.linguae.common

import androidx.room.TypeConverter
import java.util.Date

/**
 * Date converter for the database
 */
class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}