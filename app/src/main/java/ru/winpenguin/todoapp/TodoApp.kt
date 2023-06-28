package ru.winpenguin.todoapp

import android.app.Application
import androidx.room.Room
import ru.winpenguin.todoapp.data.db.AppDatabase
import ru.winpenguin.todoapp.data.db.TodoItemsRepository

class TodoApp : Application() {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-name"
        ).build()
    }
    val todoItemsRepository: TodoItemsRepository by lazy {
        TodoItemsRepository(database.todoDao())
    }
}