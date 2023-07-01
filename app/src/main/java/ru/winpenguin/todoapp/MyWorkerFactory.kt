package ru.winpenguin.todoapp

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ru.winpenguin.todoapp.data.TodoItemsRepository

class MyWorkerFactory(
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