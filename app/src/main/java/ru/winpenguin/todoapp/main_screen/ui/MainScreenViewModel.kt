package ru.winpenguin.todoapp.main_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.TodoApp
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.utils.DateFormatter

class MainScreenViewModel(
    private val repository: TodoItemsRepository,
    private val mapper: TodoItemUiStateMapper,
) : ViewModel() {

    private val doneItemsCount = MutableStateFlow(0)
    private val isDoneItemsVisible = MutableStateFlow(false)
    private val todoItems = MutableStateFlow<List<TodoItemUiState>>(emptyList())

    val uiState: Flow<MainScreenUiState> = combine(
        doneItemsCount,
        isDoneItemsVisible,
        todoItems
    ) { doneItemsCount, isDoneItemsVisible, todoItems ->
        MainScreenUiState(
            doneItemsCount = doneItemsCount,
            todoItems = todoItems,
            visibilityImageRes = if (isDoneItemsVisible) R.drawable.visibility else R.drawable.visibility_off
        )
    }

    init {
        viewModelScope.launch {
            combine(
                repository.items,
                isDoneItemsVisible
            ) { items, isDoneItemsVisible ->
                val filteredItems = if (isDoneItemsVisible) items else items.filterNot { it.isDone }
                mapper.map(filteredItems)
            }
                .collect { items ->
                    todoItems.value = items
                }
        }

        viewModelScope.launch {
            repository.items
                .map { items -> items.count { it.isDone } }
                .collect { doneItems ->
                    doneItemsCount.value = doneItems
                }
        }
    }

    fun changeCheckedState(id: String, isChecked: Boolean) {
        val item = repository.getItemById(id)
        if (item != null) {
            repository.updateItem(item.copy(isDone = isChecked))
        }
    }

    fun changeItemsVisibility() {
        isDoneItemsVisible.update { isVisible ->
            !isVisible
        }
    }

    fun removeItem(id: String) {
        repository.removeItem(id)
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