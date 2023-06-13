package ru.winpenguin.todoapp

import android.app.Application
import ru.winpenguin.todoapp.data.TodoItemsRepository

class TodoApp : Application() {
    val todoItemsRepository by lazy {
        TodoItemsRepository()
    }
}