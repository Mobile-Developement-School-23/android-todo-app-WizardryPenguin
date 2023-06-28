package ru.winpenguin.todoapp.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "importance")
    val importance: Importance,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean,

    @ColumnInfo(name = "creation_date")
    val creationDate: Instant,

    @ColumnInfo(name = "change_date")
    val changeDate: Instant,

    @ColumnInfo(name = "deadline")
    val deadline: Instant? = null,
)
