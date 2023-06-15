package ru.winpenguin.todoapp.details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.*
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
    private val mapper: DetailsScreenUiStateMapper,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private var itemId: String? = null

    private val _uiState = MutableStateFlow(DetailsScreenUiState())
    val uiState: StateFlow<DetailsScreenUiState>
        get() = _uiState.asStateFlow()

    private val _deadlineFlow = MutableStateFlow<Deadline>(Deadline.NotSelected())
    val deadlineFlow = _deadlineFlow
        .asStateFlow()
        .map { deadline ->
            when (deadline) {
                is Deadline.NotSelected -> null
                is Deadline.Selected -> dateFormatter.formatDate(deadline.date)
            }
        }
    val deadline: Deadline
        get() = _deadlineFlow.value

    fun saveTodoItem(text: String) {
        val id = itemId
        if (id == null) {
            val newItem = TodoItem(
                id = UUID.randomUUID().toString(),
                text = text,
                importance = _uiState.value.importance,
                isDone = false,
                creationDate = LocalDateTime.now(),
                deadline = deadline
            )
            repository.addItem(newItem)
        } else {
            val item = repository.getById(id)
            if (item != null) {
                repository.updateItem(
                    item.copy(
                        text = uiState.value.text,
                        importance = uiState.value.importance,
                        changeDate = LocalDateTime.now(),
                        deadline = deadline
                    )
                )
            }
        }
    }

    fun changeImportance(position: Int) {
        val importance = when (position) {
            0 -> Importance.NORMAL
            1 -> Importance.LOW
            2 -> Importance.HIGH
            else -> _uiState.value.importance
        }
        _uiState.update {
            it.copy(importance = importance)
        }
    }

    fun selectDeadline(deadline: Deadline.Selected) {
        _deadlineFlow.value = deadline
    }

    fun cancelDeadlineSelection() {
        _deadlineFlow.value = Deadline.NotSelected()
    }

    fun clearDeadline() {
        _deadlineFlow.value = Deadline.NotSelected()
    }

    fun updateCurrentItemId(itemId: String?) {
        this.itemId = itemId
        val item = if (itemId == null) null else repository.getById(itemId)
        val state = mapper.map(item)
        _uiState.value = state

        _deadlineFlow.value = item?.deadline ?: Deadline.NotSelected()
    }

    fun textChanged(text: CharSequence?) {
        _uiState.update {
            it.copy(text = text?.toString().orEmpty())
        }
    }

    fun removeTodoItem() {
        val id = itemId
        if (id != null) {
            repository.removeItem(id)
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
                    repository,
                    DetailsScreenUiStateMapper(),
                    DateFormatter()
                ) as T
            }
        }
    }
}