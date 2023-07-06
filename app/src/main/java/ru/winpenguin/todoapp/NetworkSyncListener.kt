package ru.winpenguin.todoapp

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.data.db.ItemChangeDao
import javax.inject.Inject

class NetworkSyncListener @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val todoItemsRepository: TodoItemsRepository,
    private val itemChangeDao: ItemChangeDao
) {

    private val scope = CoroutineScope(SupervisorJob() + CoroutineName("NetworkListenerScope"))

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            scope.launch {
                val hasLocallyUpdatedItems = itemChangeDao.getAllItems().isNotEmpty()
                if (hasLocallyUpdatedItems) {
                    todoItemsRepository.updateItems()
                }
            }
        }
    }

    fun start() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    fun stop() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}