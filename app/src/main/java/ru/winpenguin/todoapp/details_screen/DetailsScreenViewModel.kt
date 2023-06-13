package ru.winpenguin.todoapp.details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.winpenguin.todoapp.*
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.domain.models.Deadline
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.LocalDateTime
import java.util.*

class DetailsScreenViewModel(
    private val repository: TodoItemsRepository,
    private val dateFormatter: DateFormatter,
) : ViewModel() {

    private var importance: Importance = Importance.NORMAL

    private val _deadlineFlow = MutableStateFlow<Deadline>(Deadline.NotSelected())
    val formattedDeadlineFlow
        get() = _deadlineFlow.map { deadline ->
            when (deadline) {
                is Deadline.NotSelected -> null
                is Deadline.Selected -> dateFormatter.formatDate(deadline.date)
            }
        }
    val deadline: Deadline
        get() = _deadlineFlow.value

    fun saveTodoItem(text: String) {
        val newItem = TodoItem(
            id = UUID.randomUUID().toString(),
            text = text,
            importance = importance,
            isDone = false,
            creationDate = LocalDateTime.now(),
            deadline = deadline
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

    fun selectDeadline(deadline: Deadline) {
        _deadlineFlow.value = deadline
    }

    fun cancelDeadlineSelection() {
        _deadlineFlow.value = Deadline.NotSelected()
    }

    fun clearDeadline() {
        _deadlineFlow.value = Deadline.NotSelected()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val repository = (application as TodoApp).todoItemsRepository

                return DetailsScreenViewModel(
                    repository,
                    DateFormatter()
                ) as T
            }
        }
    }
}

