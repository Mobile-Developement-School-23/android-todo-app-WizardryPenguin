package ru.winpenguin.todoapp.data.network

import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import java.time.Instant

/**
 * Функции для маппинга из сетевых моделей в доменные и наоборот
 */

fun TodoItemDto.toDomainModel(): TodoItem {
    return TodoItem(
        id = id,
        text = text,
        importance = importance.toDomainImportance(),
        isDone = done,
        creationDate = Instant.ofEpochSecond(createdAt),
        changeDate = Instant.ofEpochSecond(changedAt),
        deadline = deadline?.let { deadline -> Instant.ofEpochSecond(deadline) }
    )
}

fun List<TodoItemDto>.toDomainModels(): List<TodoItem> {
    return map { dtoItem -> dtoItem.toDomainModel() }
}

fun TodoItem.toDto(deviceId: String): TodoItemDto {
    return TodoItemDto(
        id = id,
        text = text,
        done = isDone,
        createdAt = creationDate.epochSecond,
        changedAt = changeDate.epochSecond,
        deadline = deadline?.epochSecond,
        importance = importance.toNetworkImportance(),
        lastUpdatedBy = deviceId
    )
}

private fun String.toDomainImportance(): Importance =
    when (this) {
        NETWORK_PRIORITY_LOW -> Importance.LOW
        NETWORK_PRIORITY_BASIC -> Importance.NORMAL
        NETWORK_PRIORITY_IMPORTANT -> Importance.HIGH
        else -> Importance.NORMAL
    }

private fun Importance.toNetworkImportance(): String =
    when (this) {
        Importance.HIGH -> NETWORK_PRIORITY_IMPORTANT
        Importance.LOW -> NETWORK_PRIORITY_LOW
        Importance.NORMAL -> NETWORK_PRIORITY_BASIC
    }

private const val NETWORK_PRIORITY_LOW = "low"
private const val NETWORK_PRIORITY_BASIC = "basic"
private const val NETWORK_PRIORITY_IMPORTANT = "important"
