package ru.winpenguin.todoapp.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import ru.winpenguin.todoapp.workers.CustomWorkerFactory

@Module
object WorkerModule {

    @Provides
    fun provideWorkManagerConfiguration(customWorkerFactory: CustomWorkerFactory): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(customWorkerFactory)
            .build()
    }

    @Provides
    fun provideWorkManager(context: Context) = WorkManager.getInstance(context.applicationContext)
}