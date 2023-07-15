package ru.winpenguin.todoapp.details_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import ru.winpenguin.todoapp.utils.ZoneIdProvider
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * Хранит состояние экрана деталей дела,
 * Обрабатывает действия пользователя на данном экране, изменяя дела
 */
class DetailsScreenViewModel(
    private val repository: TodoItemsRepository,
    private val mapper: DetailsScreenUiStateMapper,
    private val dateFormatter: DateFormatter,
    private val zoneIdProvider: ZoneIdProvider,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    @Volatile
    private var itemId: String? = null

    private val _uiState = MutableStateFlow(DetailsScreenUiState())
    val uiState: Flow<DetailsScreenUiState>
        get() = _uiState.asStateFlow()
            .onEach { Log.d("TAG", "emit $it") }

    private var deadline: Instant? = null

    fun saveTodoItem() {
        viewModelScope.launch(defaultDispatcher) {
            val id = itemId
            if (id == null) {
                val newItem = createTodoItem()
                repository.addItem(newItem)
            } else {
                updateTodoItem(id)
            }
        }
    }

    private suspend fun updateTodoItem(id: String) {
        val item = repository.getItemById(id) ?: return
        repository.updateItem(
            item.copy(
                text = _uiState.value.description,
                importance = _uiState.value.importance,
                changeDate = Instant.now(),
                deadline = deadline
            )
        )
    }

    private fun createTodoItem(): TodoItem {
        val creationDate = Instant.now()
        return TodoItem(
            id = UUID.randomUUID().toString(),
            text = _uiState.value.description,
            importance = _uiState.value.importance,
            isDone = false,
            creationDate = creationDate,
            changeDate = creationDate,
            deadline = deadline
        )
    }

    fun changeImportance(importance: Importance) {
        _uiState.update {
            it.copy(importance = importance)
        }
    }

    fun selectDeadline(deadline: LocalDate) {
        val instant = deadline.atStartOfDay(zoneIdProvider.zoneId().invoke()).toInstant()
        this.deadline = instant
        _uiState.update {
            it.copy(deadline = dateFormatter.formatDate(instant))
        }
    }

    fun updateCurrentItemId(itemId: String?) {
        viewModelScope.launch(defaultDispatcher) {
            this@DetailsScreenViewModel.itemId = itemId
            val item = if (itemId == null) null else repository.getItemById(itemId)
            val state = mapper.map(item)
            _uiState.value = state

            deadline = item?.deadline
        }
    }

    fun textChanged(text: String) {
        _uiState.update {
            it.copy(description = text)
        }
    }

    fun removeTodoItem() {
        viewModelScope.launch {
            itemId?.let { id -> repository.removeItem(id) }
        }
    }

    fun changeDeadlineChecked(isChecked: Boolean) {
        _uiState.update {
            it.copy(
                isDeadlineChecked = isChecked,
                isCalendarVisible = isChecked,
                deadline = if (isChecked) it.deadline else null
            )
        }
        if (!isChecked) {
            deadline = null
        }
    }

    fun calendarClosed() {
        viewModelScope.launch {
            delay(5)
            _uiState.update {
                it.copy(
                    isDeadlineChecked = deadline != null,
                    isCalendarVisible = false
                )
            }
        }
    }
}