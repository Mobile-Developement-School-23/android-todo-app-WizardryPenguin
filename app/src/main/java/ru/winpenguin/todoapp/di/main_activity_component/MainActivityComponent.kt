package ru.winpenguin.todoapp.di.main_activity_component

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import ru.winpenguin.todoapp.MainActivity
import ru.winpenguin.todoapp.common.ViewModelFactory
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityComponent

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext

@ActivityComponent
@Subcomponent(
    modules = [
        ViewModelModule::class,
        ConnectivityModule::class
    ]
)
interface MainActivityComponent {

    val viewModelFactory: ViewModelFactory

    fun inject(mainActivity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @ActivityContext
            context: Context
        ): MainActivityComponent
    }
}