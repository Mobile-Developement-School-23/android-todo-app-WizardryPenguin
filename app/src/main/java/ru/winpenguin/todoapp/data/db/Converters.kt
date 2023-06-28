package ru.winpenguin.todoapp.data.db

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun epochMillisToInstant(epochMillis: Long?): Instant? {
        epochMillis ?: return null
        return Instant.ofEpochMilli(epochMillis)
    }

    @TypeConverter
    fun instantToEpochMillis(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }
}