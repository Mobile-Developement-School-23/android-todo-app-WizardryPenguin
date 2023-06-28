package ru.winpenguin.todoapp

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.winpenguin.todoapp.data.DeviceIdRepository
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.data.db.AppDatabase
import ru.winpenguin.todoapp.data.network.AuthInterceptor
import ru.winpenguin.todoapp.data.network.TodoApi

private const val BASE_URL = "https://beta.mrdekk.ru/"

class TodoApp : Application() {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-name"
        ).build()
    }
    val todoItemsRepository: TodoItemsRepository by lazy {
        TodoItemsRepository(database.todoDao(), todoApi, deviceIdRepository)
    }

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = BODY
        }
    }
    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor()
    }
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    val todoApi: TodoApi by lazy {
        retrofit.create(TodoApi::class.java)
    }

    private val sharedPreferences: SharedPreferences
        get() = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)

    private val deviceIdRepository by lazy {
        DeviceIdRepository(sharedPreferences)
    }
}