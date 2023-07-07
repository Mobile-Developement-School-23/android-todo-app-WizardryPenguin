package ru.winpenguin.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.winpenguin.todoapp.databinding.ActivityMainBinding
import ru.winpenguin.todoapp.di.main_activity_component.MainActivityComponent
import ru.winpenguin.todoapp.workers.SyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var networkSyncListener: NetworkSyncListener

    lateinit var activityComponent: MainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as TodoApp).appComponent
            .mainActivityComponent()
            .create(this)
        activityComponent.inject(this)

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