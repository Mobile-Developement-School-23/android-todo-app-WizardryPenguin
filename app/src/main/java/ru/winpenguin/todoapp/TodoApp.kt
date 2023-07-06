package ru.winpenguin.todoapp

import android.app.Application
import androidx.work.Configuration
import ru.winpenguin.todoapp.di.ApplicationComponent
import ru.winpenguin.todoapp.di.DaggerApplicationComponent
import javax.inject.Inject

class TodoApp : Application(), Configuration.Provider {

    lateinit var appComponent: ApplicationComponent

    @Inject
    lateinit var workerConfiguration: Configuration

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .factory()
            .create(this)
        appComponent.inject(this)
    }

    override fun getWorkManagerConfiguration(): Configuration = workerConfiguration
}