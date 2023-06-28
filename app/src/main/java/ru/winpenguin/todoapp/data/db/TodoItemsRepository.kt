package ru.winpenguin.todoapp.data.db

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.winpenguin.todoapp.domain.models.TodoItem

class TodoItemsRepository(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val items: Flow<List<TodoItem>> = todoDao.allItemsFlow()

    suspend fun getItemById(id: String): TodoItem? {
        return withContext(ioDispatcher) {
            todoDao.getItemById(id)
        }
    }

    suspend fun addItem(item: TodoItem) {
        withContext(ioDispatcher) {
            todoDao.insertItem(item)
        }
    }

    suspend fun updateItem(updatedItem: TodoItem) {
        withContext(ioDispatcher) {
            todoDao.updateItem(updatedItem)
        }
    }

    suspend fun removeItem(id: String) {
        withContext(ioDispatcher) {
            todoDao.deleteItem(id)
        }
    }
}