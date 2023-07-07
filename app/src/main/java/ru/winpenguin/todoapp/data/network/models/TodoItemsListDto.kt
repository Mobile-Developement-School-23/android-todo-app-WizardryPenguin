package ru.winpenguin.todoapp.data.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.winpenguin.todoapp.data.network.TodoItemDto

@JsonClass(generateAdapter = true)
data class TodoItemsListDto(
    @Json(name = "list")
    val list: List<TodoItemDto>,

    @Json(name = "revision")
    val revision: Int
)