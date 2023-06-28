package ru.winpenguin.todoapp.data.db

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun epochMillisToInstant(epochSeconds: Long?): Instant? {
        epochSeconds ?: return null
        return Instant.ofEpochSecond(epochSeconds)
    }

    @TypeConverter
    fun instantToEpochSeconds(instant: Instant?): Long? {
        return instant?.epochSecond
    }
}