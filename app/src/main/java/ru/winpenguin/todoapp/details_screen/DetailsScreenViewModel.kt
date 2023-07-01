package ru.winpenguin.todoapp.details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.*
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.domain.models.Importance
import ru.winpenguin.todoapp.domain.models.TodoItem
import ru.winpenguin.todoapp.utils.DateFormatter
import java.time.Instant
import java.util.*

class DetailsScreenViewModel(
    private val repository: TodoItemsRepository,
    private val mapper: DetailsScreenUiStateMapper,
    private val dateFormatter: DateFormatter,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    @Volatile
    private var itemId: String? = null

    private val _uiState = MutableStateFlow(DetailsScreenUiState())
    val uiState: StateFlow<DetailsScreenUiState>
        get() = _uiState.asStateFlow()

    var deadline: Instant? = null
        private set
    private val deadlineTrigger = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val deadlineFlow = deadlineTrigger
        .map {
            val deadline = deadline
            deadline?.let { dateFormatter.formatDate(deadline) }
        }
        .flowOn(defaultDispatcher)

    fun saveTodoItem(text: String) {
        viewModelScope.launch(defaultDispatcher) {
            val id = itemId
            if (id == null) {
                val creationDate = Instant.now()
                val newItem = TodoItem(
                    id = UUID.randomUUID().toString(),
                    text = text,
                    importance = _uiState.value.importance,
                    isDone = false,
                    creationDate = creationDate,
                    changeDate = creationDate,
                    deadline = deadline
                )
                repository.addItem(newItem)
            } else {
                val item = repository.getItemById(id)
                if (item != null) {
                    repository.updateItem(
                        item.copy(
                            text = uiState.value.text,
                            importance = uiState.value.importance,
                            changeDate = Instant.now(),
                            deadline = deadline
                        )
                    )
                }
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

    fun selectDeadline(deadline: Instant) {
        this.deadline = deadline
        deadlineTrigger.tryEmit(Unit)
    }

    fun cancelDeadlineSelection() {
        deadline = null
        deadlineTrigger.tryEmit(Unit)
    }

    fun clearDeadline() {
        deadline = null
        deadlineTrigger.tryEmit(Unit)
    }

    fun updateCurrentItemId(itemId: String?) {
        viewModelScope.launch(defaultDispatcher) {
            this@DetailsScreenViewModel.itemId = itemId
            val item = if (itemId == null) null else repository.getItemById(itemId)
            val state = mapper.map(item)
            _uiState.value = state

            deadline = item?.deadline
            deadlineTrigger.tryEmit(Unit)
        }
    }

    fun textChanged(text: CharSequence?) {
        _uiState.update {
            it.copy(text = text?.toString().orEmpty())
        }
    }

    fun removeTodoItem() {
        viewModelScope.launch {
            itemId?.let { id -> repository.removeItem(id) }
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