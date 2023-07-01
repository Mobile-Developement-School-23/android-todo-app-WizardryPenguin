package ru.winpenguin.todoapp

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.data.db.ItemChangeDao
import ru.winpenguin.todoapp.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val connectivityManager: ConnectivityManager
        get() = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    private val todoItemsRepository: TodoItemsRepository
        get() = (application as TodoApp).todoItemsRepository
    private val itemChangeDao: ItemChangeDao
        get() = (application as TodoApp).itemChangeDao
    private val networkSyncListener by lazy {
        NetworkSyncListener(connectivityManager, todoItemsRepository, itemChangeDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enqueueSync()
        networkSyncListener.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkSyncListener.stop()
    }

    private fun enqueueSync() {
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(8, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SYNC_TODO_ITEMS_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    companion object {
        private const val SYNC_TODO_ITEMS_WORK_NAME = "syncTodoItems"
    }
}