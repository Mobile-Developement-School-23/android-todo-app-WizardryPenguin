package ru.winpenguin.todoapp.di

import android.app.Application.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.data.db.AppDatabase
import ru.winpenguin.todoapp.data.db.ItemChangeDao
import ru.winpenguin.todoapp.data.db.TodoDao
import javax.inject.Singleton

@Module
object PersistenceModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database"
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao = db.todoDao()

    @Singleton
    @Provides
    fun provideItemChangeDao(db: AppDatabase): ItemChangeDao = db.itemChangeDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext applicationContext: Context): SharedPreferences {
        val key = applicationContext.getString(R.string.preference_file_key)
        return applicationContext.getSharedPreferences(key, MODE_PRIVATE)
    }
}

