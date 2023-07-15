package ru.winpenguin.todoapp.di.main_activity_component

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides

@Module
object ConnectivityModule {

    @Provides
    fun connectivityManager(@ActivityContext context: Context): ConnectivityManager {
        return context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    }
}