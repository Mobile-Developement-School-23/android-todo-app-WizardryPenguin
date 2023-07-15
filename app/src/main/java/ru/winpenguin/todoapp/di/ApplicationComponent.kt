package ru.winpenguin.todoapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.winpenguin.todoapp.TodoApp
import ru.winpenguin.todoapp.di.main_activity_component.MainActivityComponent
import ru.winpenguin.todoapp.di.main_activity_component.ViewModelModule
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@Singleton
@Component(
    modules = [
        PersistenceModule::class,
        NetworkModule::class,
        DispatchersModule::class,
        DateTimeModule::class,
        ViewModelModule::class,
        WorkerModule::class,
        SubcomponentsModule::class
    ]
)
interface ApplicationComponent {

    fun inject(todoApp: TodoApp)

    fun mainActivityComponent(): MainActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @ApplicationContext
            applicationContext: Context
        ): ApplicationComponent
    }
}