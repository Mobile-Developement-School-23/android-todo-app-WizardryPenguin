package ru.winpenguin.todoapp

import java.time.LocalDate
import java.time.LocalDateTime

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val isDone: Boolean,
    val creationDate: LocalDateTime,
    val changeDate: LocalDateTime? = null,
    val deadline: LocalDate? = null,
)
