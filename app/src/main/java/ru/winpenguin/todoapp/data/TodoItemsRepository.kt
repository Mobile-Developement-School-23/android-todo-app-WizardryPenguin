package ru.winpenguin.todoapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.winpenguin.todoapp.data.RequestType.AddItem
import ru.winpenguin.todoapp.data.RequestType.RemoveItem
import ru.winpenguin.todoapp.data.RequestType.UpdateItem
import ru.winpenguin.todoapp.data.db.ChangeType.ADD
import ru.winpenguin.todoapp.data.db.ChangeType.REMOVE
import ru.winpenguin.todoapp.data.db.ChangeType.UPDATE
import ru.winpenguin.todoapp.data.db.ChangedItemEntity
import ru.winpenguin.todoapp.data.db.ItemChangeDao
import ru.winpenguin.todoapp.data.db.TodoDao
import ru.winpenguin.todoapp.data.network.NetworkError
import ru.winpenguin.todoapp.data.network.NetworkError.AddItemError
import ru.winpenguin.todoapp.data.network.NetworkError.AuthorizationError
import ru.winpenguin.todoapp.data.network.NetworkError.ConnectionError
import ru.winpenguin.todoapp.data.network.NetworkError.OtherError
import ru.winpenguin.todoapp.data.network.NetworkError.RemoveItemError
import ru.winpenguin.todoapp.data.network.NetworkError.UpdateItemError
import ru.winpenguin.todoapp.data.network.SC_INCORRECT_AUTHORIZATION
import ru.winpenguin.todoapp.data.network.SC_INCORRECT_REQUEST
import ru.winpenguin.todoapp.data.network.SC_ITEM_NOT_FOUND
import ru.winpenguin.todoapp.data.network.TodoApi
import ru.winpenguin.todoapp.data.network.models.SingleTodoItemDto
import ru.winpenguin.todoapp.data.network.toDomainModels
import ru.winpenguin.todoapp.data.network.toDto
import ru.winpenguin.todoapp.di.IoDispatcher
import ru.winpenguin.todoapp.domain.models.TodoItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoItemsRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val itemChangeDao: ItemChangeDao,
    private val todoApi: TodoApi,
    private val deviceIdRepository: DeviceIdRepository,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
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
            updateItems()
        }
    }

    suspend fun updateItems(): Boolean {
        return try {
            val response = todoApi.getAllItems()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                revisionFlow.emit(dto.revision)
                val remoteTodoItems = dto.list.toDomainModels()

                updateRemoteItemsWithLocalChanges(remoteTodoItems)

                val newResponse = todoApi.getAllItems()
                if (newResponse.isSuccessful && newResponse.body() != null) {
                    val newDto = newResponse.body()!!
                    revisionFlow.emit(newDto.revision)
                    val newRemoteTodoItems = newDto.list.toDomainModels()

                    val serverIds = newRemoteTodoItems.map { item -> item.id }.toSet()
                    val dbIds = items.first().map { item -> item.id }.toSet()
                    val idsToRemove = dbIds.subtract(serverIds)
                    todoDao.deleteItems(idsToRemove)

                    todoDao.insertItems(newRemoteTodoItems)
                    true
                } else {
                    when (response.code()) {
                        SC_INCORRECT_AUTHORIZATION -> _errorFlow.emit(AuthorizationError)
                        else -> _errorFlow.emit(OtherError)
                    }
                    false
                }
            } else {
                when (response.code()) {
                    SC_INCORRECT_AUTHORIZATION -> _errorFlow.emit(AuthorizationError)
                    else -> _errorFlow.emit(OtherError)
                }
                false
            }
        } catch (e: Exception) {
            _errorFlow.emit(ConnectionError)
            false
        }
    }

    private suspend fun updateRemoteItemsWithLocalChanges(
        remoteTodoItems: List<TodoItem>
    ) {
        val localChangedItems = itemChangeDao.getAllItems().sorted()
        localChangedItems.forEach { item ->
            when (item.changeType) {
                ADD -> {
                    val localItem = todoDao.getItemById(item.itemId)
                    if (localItem != null) {
                        addNetworkItem(localItem)
                    } else {
                        itemChangeDao.deleteItem(item.itemId)
                    }
                }

                UPDATE -> {
                    val remoteItem = remoteTodoItems.firstOrNull { it.id == item.itemId }
                    if (remoteItem != null) {
                        val localItem = todoDao.getItemById(item.itemId)
                        if (localItem != null && localItem.changeDate.isAfter(remoteItem.changeDate)) {
                            updateNetworkItem(localItem)
                        } else {
                            itemChangeDao.deleteItem(item.itemId)
                        }
                    } else {
                        itemChangeDao.deleteItem(item.itemId)
                    }
                }

                REMOVE -> {
                    val localItem = todoDao.getItemById(item.itemId)
                    if (localItem != null) {
                        removeNetworkItem(item.itemId)
                    } else {
                        itemChangeDao.deleteItem(item.itemId)
                    }
                }

            }
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
            itemChangeDao.insertItem(
                ChangedItemEntity(
                    itemId = item.id,
                    changeType = ADD
                )
            )
            addNetworkItem(item)
        }
    }

    private suspend fun addNetworkItem(item: TodoItem) {
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

    suspend fun updateItem(updatedItem: TodoItem) {
        scope.launch {
            todoDao.updateItem(updatedItem)
            itemChangeDao.insertItem(
                ChangedItemEntity(
                    itemId = updatedItem.id,
                    changeType = UPDATE
                )
            )
            updateNetworkItem(updatedItem)
        }
    }

    private suspend fun updateNetworkItem(updatedItem: TodoItem) {
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

    suspend fun removeItem(id: String) {
        scope.launch {
            todoDao.deleteItem(id)
            itemChangeDao.insertItem(
                ChangedItemEntity(
                    itemId = id,
                    changeType = REMOVE
                )
            )
            removeNetworkItem(id)
        }
    }

    private suspend fun removeNetworkItem(id: String) {
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

    private suspend fun handleResponse(
        response: Response<SingleTodoItemDto>,
        requestType: RequestType
    ) {
        if (response.isSuccessful && response.body() != null) {
            itemChangeDao.deleteItem(response.body()!!.element.id)
            revisionFlow.emit(response.body()?.revision)
        } else {
            when (response.code()) {
                SC_INCORRECT_REQUEST,
                SC_ITEM_NOT_FOUND -> {
                    updateItems()
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
                    itemChangeDao.deleteItem(response.body()!!.element.id)
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