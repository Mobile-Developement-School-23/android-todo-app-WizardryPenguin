package ru.winpenguin.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.winpenguin.todoapp.domain.models.TodoItem

@Database(entities = [TodoItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}