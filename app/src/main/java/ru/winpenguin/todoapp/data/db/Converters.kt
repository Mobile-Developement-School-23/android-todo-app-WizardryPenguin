package ru.winpenguin.todoapp.data.db

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Конвертирует доменные модели в сохраняемые в базе данных и наоборот
 */
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