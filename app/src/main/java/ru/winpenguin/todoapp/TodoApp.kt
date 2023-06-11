package ru.winpenguin.todoapp

import android.app.Application

class TodoApp : Application() {
    val todoItemsRepository by lazy {
        TodoItemsRepository()
    }
}