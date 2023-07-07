package ru.winpenguin.todoapp.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TodoItemDto(
    @Json(name = "id")
    val id: String,

    @Json(name = "text")
    val text: String,

    @Json(name = "done")
    val done: Boolean,

    @Json(name = "created_at")
    val createdAt: Long,

    @Json(name = "changed_at")
    val changedAt: Long,

    @Json(name = "deadline")
    val deadline: Long?,

    @Json(name = "importance")
    val importance: String,

    @Json(name = "last_updated_by")
    val lastUpdatedBy: String,

    @Json(name = "color")
    val color: String? = null,
)