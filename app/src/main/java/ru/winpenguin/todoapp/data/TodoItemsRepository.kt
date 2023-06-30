package ru.winpenguin.todoapp.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import retrofit2.Response
import ru.winpenguin.todoapp.data.RequestType.*
import ru.winpenguin.todoapp.data.db.TodoDao
import ru.winpenguin.todoapp.data.network.*
import ru.winpenguin.todoapp.data.network.NetworkError.*
import ru.winpenguin.todoapp.data.network.models.SingleTodoItemDto
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

    private val _errorFlow = MutableStateFlow<NetworkError?>(null)
    val errorFlow: Flow<NetworkError> = _errorFlow.filterNotNull()

    val items: Flow<List<TodoItem>> = todoDao.allItemsFlow()

    init {
        scope.launch {
            updateItemsFromNetwork()
        }
    }

    private suspend fun updateItemsFromNetwork() {
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
                when (response.code()) {
                    SC_INCORRECT_AUTHORIZATION -> _errorFlow.emit(AuthorizationError)
                    else -> _errorFlow.emit(OtherError)
                }
            }
        } catch (e: Exception) {
            _errorFlow.emit(ConnectionError)
        }
    }

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
                handleResponse(
                    response = response,
                    requestType = AddItem(item)
                )
            } catch (e: Exception) {
                _errorFlow.emit(ConnectionError)
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
                handleResponse(
                    response = response,
                    requestType = UpdateItem(updatedItem)
                )
            } catch (e: Exception) {
                _errorFlow.emit(ConnectionError)
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
                handleResponse(
                    response = response,
                    requestType = RemoveItem(id)
                )
            } catch (e: Exception) {
                _errorFlow.emit(ConnectionError)
            }
        }
    }

    private suspend fun handleResponse(
        response: Response<SingleTodoItemDto>,
        requestType: RequestType
    ) {
        if (response.isSuccessful && response.body() != null) {
            revisionFlow.emit(response.body()?.revision)
        } else {
            when (response.code()) {
                SC_INCORRECT_REQUEST,
                SC_ITEM_NOT_FOUND -> {
                    updateItemsFromNetwork()
                    val newResponse = when (requestType) {
                        is AddItem -> todoApi.addNewItem(
                            revision = getRevision(),
                            item = SingleTodoItemDto(
                                element = requestType.item.toDto(deviceIdRepository.getDeviceId()),
                                revision = getRevision()
                            )
                        )
                        is UpdateItem -> todoApi.changeItem(
                            revision = getRevision(),
                            id = requestType.item.id,
                            item = SingleTodoItemDto(
                                element = requestType.item.toDto(deviceIdRepository.getDeviceId()),
                                revision = getRevision()
                            )
                        )
                        is RemoveItem -> todoApi.deleteItem(
                            revision = getRevision(),
                            id = requestType.id
                        )
                    }
                    if (newResponse.isSuccessful && newResponse.body() != null) {
                        revisionFlow.emit(newResponse.body()?.revision)
                    } else {
                        when (requestType) {
                            is AddItem -> _errorFlow.emit(AddItemError)
                            is UpdateItem -> _errorFlow.emit(UpdateItemError)
                            is RemoveItem -> _errorFlow.emit(RemoveItemError)
                        }
                    }
                }
                SC_INCORRECT_AUTHORIZATION -> _errorFlow.emit(AuthorizationError)
                else -> _errorFlow.emit(OtherError)
            }
        }
    }

    fun clearError() {
        _errorFlow.tryEmit(null)
    }

    private suspend fun getRevision(): Int = revisionFlow.filterNotNull().first()
}