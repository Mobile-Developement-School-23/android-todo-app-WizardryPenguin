package ru.winpenguin.todoapp.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ru.winpenguin.todoapp.data.TodoItemsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomWorkerFactory @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> SyncWorker(
                appContext,
                workerParameters,
                todoItemsRepository
            )
            else -> null
        }
    }
}