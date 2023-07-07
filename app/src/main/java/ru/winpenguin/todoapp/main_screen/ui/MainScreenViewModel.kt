package ru.winpenguin.todoapp.main_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.data.network.NetworkError.AddItemError
import ru.winpenguin.todoapp.data.network.NetworkError.AuthorizationError
import ru.winpenguin.todoapp.data.network.NetworkError.ConnectionError
import ru.winpenguin.todoapp.data.network.NetworkError.OtherError
import ru.winpenguin.todoapp.data.network.NetworkError.RemoveItemError
import ru.winpenguin.todoapp.data.network.NetworkError.UpdateItemError
import ru.winpenguin.todoapp.main_screen.ui.MainScreenEvent.ShowMessage
import java.time.Instant

/**
 * Хранит состояние основного экрана дела.
 * Обрабатывает действия пользователя на экране: изменяет дела и выдает ошибку,
 * когда есть неполадки при совершении действия
 */
class MainScreenViewModel(
    private val repository: TodoItemsRepository,
    private val mapper: TodoItemUiStateMapper,
    private val defaultDispatcher: CoroutineDispatcher
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
        .flowOn(defaultDispatcher)

    val events: Flow<MainScreenEvent> = repository.errorFlow.map { error ->
        when (error) {
            is ConnectionError -> ShowMessage(R.string.no_network)
            is AuthorizationError -> ShowMessage(R.string.authorization_error)
            OtherError -> ShowMessage(R.string.other_error)
            AddItemError -> ShowMessage(R.string.add_item_error)
            UpdateItemError -> ShowMessage(R.string.update_item_error)
            RemoveItemError -> ShowMessage(R.string.remove_item_error)
        }
    }
        .flowOn(defaultDispatcher)

    init {
        viewModelScope.launch {
            combine(
                repository.items,
                isDoneItemsVisible
            ) { items, isDoneItemsVisible ->
                val filteredItems = if (isDoneItemsVisible) items else items.filterNot { it.isDone }
                mapper.map(filteredItems)
            }
                .flowOn(defaultDispatcher)
                .collect { items ->
                    todoItems.value = items
                }
        }

        viewModelScope.launch {
            repository.items
                .map { items -> items.count { it.isDone } }
                .flowOn(defaultDispatcher)
                .collect { doneItems ->
                    doneItemsCount.value = doneItems
                }
        }
    }

    fun changeCheckedState(id: String, isChecked: Boolean) {
        viewModelScope.launch {
            val item = repository.getItemById(id)
            if (item != null) {
                repository.updateItem(
                    item.copy(isDone = isChecked, changeDate = Instant.now())
                )
            }
        }
    }

    fun invertCheckedState(id: String) {
        viewModelScope.launch {
            val item = repository.getItemById(id)
            if (item != null) {
                repository.updateItem(
                    item.copy(isDone = !item.isDone, changeDate = Instant.now())
                )
            }
        }
    }

    fun changeItemsVisibility() {
        isDoneItemsVisible.update { isVisible ->
            !isVisible
        }
    }

    fun removeItem(id: String) {
        viewModelScope.launch {
            repository.removeItem(id)
        }
    }

    fun onMessageShown() {
        repository.clearError()
    }
}