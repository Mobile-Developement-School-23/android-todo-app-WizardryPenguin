package ru.winpenguin.todoapp.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import ru.winpenguin.todoapp.data.db.TodoDao
import ru.winpenguin.todoapp.data.network.TodoApi
import ru.winpenguin.todoapp.data.network.models.SingleTodoItemDto
import ru.winpenguin.todoapp.data.network.toDomainModels
import ru.winpenguin.todoapp.data.network.toDto
import ru.winpenguin.todoapp.domain.models.TodoItem

class TodoItemsRepository(
    private val todoDao: TodoDao,
    private val todoApi: TodoApi,
    private val deviceIdRepository: DeviceIdRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val scope = CoroutineScope(
        SupervisorJob() + ioDispatcher + CoroutineName("TodoItemsRepositoryScope")
    )

    private val revisionFlow = MutableStateFlow<Int?>(null)

    init {
        scope.launch {
            try {
                val response = todoApi.getAllItems()
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { dto ->
                        revisionFlow.emit(dto.revision)
                        val todoItems = dto.list.toDomainModels()

                        val serverIds = todoItems.map { item -> item.id }.toSet()
                        val dbIds = items.first().map { item -> item.id }.toSet()
                        val idsToRemove = dbIds.subtract(serverIds)
                        todoDao.deleteItems(idsToRemove)

                        todoDao.insertItems(todoItems)
                    }
                } else {
                    //retry?
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val items: Flow<List<TodoItem>> = todoDao.allItemsFlow()

    suspend fun getItemById(id: String): TodoItem? {
        return withContext(ioDispatcher) {
            todoDao.getItemById(id)
        }
    }

    suspend fun addItem(item: TodoItem) {
        scope.launch {
            todoDao.insertItem(item)
        }
        scope.launch {
            try {
                val response = todoApi.addNewItem(
                    revision = getRevision(),
                    item = SingleTodoItemDto(
                        element = item.toDto(deviceIdRepository.getDeviceId()),
                        revision = getRevision()
                    )
                )
                if (response.isSuccessful && response.body() != null) {
                    revisionFlow.emit(response.body()?.revision)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateItem(updatedItem: TodoItem) {
        scope.launch {
            todoDao.updateItem(updatedItem)
        }
        scope.launch {
            try {
                val response = todoApi.changeItem(
                    revision = getRevision(),
                    id = updatedItem.id,
                    item = SingleTodoItemDto(
                        element = updatedItem.toDto(deviceIdRepository.getDeviceId()),
                        revision = getRevision()
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    revisionFlow.emit(response.body()?.revision)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun removeItem(id: String) {
        scope.launch {
            todoDao.deleteItem(id)
        }
        scope.launch {
            try {
                val response = todoApi.deleteItem(
                    revision = getRevision(),
                    id = id
                )
                if (response.isSuccessful && response.body() != null) {
                    revisionFlow.emit(response.body()?.revision)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getRevision(): Int = revisionFlow.filterNotNull().first()
}