package ru.winpenguin.todoapp.main_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.TodoApp
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.utils.DateFormatter

class MainScreenViewModel(
    private val repository: TodoItemsRepository,
    private val mapper: TodoItemUiStateMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.items
                .map { items -> mapper.map(items) }
                .collect { items ->
                    _uiState.update {
                        it.copy(todoItems = items)
                    }
                }
        }

        viewModelScope.launch {
            repository.items
                .map { items -> items.count { it.isDone } }
                .collect { doneItems ->
                    _uiState.update {
                        it.copy(doneItemsCount = doneItems)
                    }
                }
        }
    }

    fun changeCheckedState(id: String, isChecked: Boolean) {
        val item = repository.getById(id)
        if (item != null) {
            repository.updateItem(item.copy(isDone = isChecked))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val repository = (application as TodoApp).todoItemsRepository

                return MainScreenViewModel(
                    repository,
                    TodoItemUiStateMapper(DateFormatter())
                ) as T
            }
        }
    }
}