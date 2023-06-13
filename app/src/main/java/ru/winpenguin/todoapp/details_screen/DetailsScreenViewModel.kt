package ru.winpenguin.todoapp.details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.winpenguin.todoapp.Importance
import ru.winpenguin.todoapp.TodoApp
import ru.winpenguin.todoapp.TodoItem
import ru.winpenguin.todoapp.TodoItemsRepository
import java.time.LocalDateTime
import java.util.*

class DetailsScreenViewModel(
    private val repository: TodoItemsRepository,
) : ViewModel() {

    private var importance: Importance = Importance.NORMAL

    fun saveTodoItem(text: String) {
        val newItem = TodoItem(
            id = UUID.randomUUID().toString(),
            text = text,
            importance = importance,
            isDone = false,
            creationDate = LocalDateTime.now(),
            deadline = null
        )
        repository.addItem(newItem)
    }

    fun changeImportance(position: Int) {
        importance = when (position) {
            0 -> Importance.NORMAL
            1 -> Importance.LOW
            2 -> Importance.HIGH
            else -> importance
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val repository = (application as TodoApp).todoItemsRepository

                return DetailsScreenViewModel(
                    repository
                ) as T
            }
        }
    }
}