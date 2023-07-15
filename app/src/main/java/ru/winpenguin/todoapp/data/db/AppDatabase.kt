package ru.winpenguin.todoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.winpenguin.todoapp.domain.models.TodoItem

/**
 * База данных приложения: хранит данные
 */
@Database(entities = [TodoItem::class, ChangedItemEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun itemChangeDao(): ItemChangeDao
}