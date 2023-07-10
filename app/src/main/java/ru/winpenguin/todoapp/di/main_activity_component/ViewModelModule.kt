package ru.winpenguin.todoapp.di.main_activity_component

import androidx.lifecycle.ViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineDispatcher
import ru.winpenguin.todoapp.data.TodoItemsRepository
import ru.winpenguin.todoapp.details_screen.DetailsScreenUiStateMapper
import ru.winpenguin.todoapp.details_screen.DetailsScreenViewModel
import ru.winpenguin.todoapp.di.DefaultDispatcher
import ru.winpenguin.todoapp.main_screen.MainScreenViewModel
import ru.winpenguin.todoapp.main_screen.TodoItemUiStateMapper
import ru.winpenguin.todoapp.settings_screen.SettingsViewModel
import ru.winpenguin.todoapp.theme.ThemeRepository
import ru.winpenguin.todoapp.utils.DateFormatter
import ru.winpenguin.todoapp.utils.ZoneIdProvider
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
object ViewModelModule {

    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    @Provides
    fun provideMainScreenViewModel(
        repository: TodoItemsRepository,
        todoItemsUiStateMapper: TodoItemUiStateMapper,
        @DefaultDispatcher
        defaultDispatcher: CoroutineDispatcher
    ): ViewModel {
        return MainScreenViewModel(
            repository = repository,
            mapper = todoItemsUiStateMapper,
            defaultDispatcher = defaultDispatcher
        )
    }

    @IntoMap
    @ViewModelKey(DetailsScreenViewModel::class)
    @Provides
    fun provideDetailsScreenViewModel(
        repository: TodoItemsRepository,
        detailsScreenUiStateMapper: DetailsScreenUiStateMapper,
        dateFormatter: DateFormatter,
        zoneIdProvider: ZoneIdProvider,
        @DefaultDispatcher
        defaultDispatcher: CoroutineDispatcher
    ): ViewModel {
        return DetailsScreenViewModel(
            repository = repository,
            mapper = detailsScreenUiStateMapper,
            dateFormatter = dateFormatter,
            zoneIdProvider = zoneIdProvider,
            defaultDispatcher = defaultDispatcher
        )
    }

    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    @Provides
    fun provideSettingsScreenViewModel(
        themeRepository: ThemeRepository,
    ): ViewModel {
        return SettingsViewModel(
            themeRepository = themeRepository,
        )
    }
}

