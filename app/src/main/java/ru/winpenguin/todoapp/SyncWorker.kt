package ru.winpenguin.todoapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.winpenguin.todoapp.data.TodoItemsRepository

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val todoItemsRepository: TodoItemsRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val isSuccessful = todoItemsRepository.updateItems()
        return if (isSuccessful) {
            Result.success()
        } else {
            Result.failure()
        }
    }

}