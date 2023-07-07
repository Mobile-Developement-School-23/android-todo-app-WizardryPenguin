package ru.winpenguin.todoapp.data

import ru.winpenguin.todoapp.domain.models.TodoItem

sealed class RequestType {
    data class AddItem(val item: TodoItem) : RequestType()

    data class UpdateItem(val item: TodoItem) : RequestType()

    data class RemoveItem(val id: String) : RequestType()
}