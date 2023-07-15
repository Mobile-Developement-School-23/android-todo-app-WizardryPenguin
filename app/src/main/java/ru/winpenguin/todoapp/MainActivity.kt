package ru.winpenguin.todoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.winpenguin.todoapp.common.ViewModelFactory
import ru.winpenguin.todoapp.details_screen.DetailsComposeScreen
import ru.winpenguin.todoapp.details_screen.DetailsScreenViewModel
import ru.winpenguin.todoapp.di.main_activity_component.MainActivityComponent
import ru.winpenguin.todoapp.main_screen.MainScreenCompose
import ru.winpenguin.todoapp.main_screen.MainScreenViewModel
import ru.winpenguin.todoapp.navigation.NavigationDestination.DetailsScreen
import ru.winpenguin.todoapp.navigation.NavigationDestination.DetailsScreen.NAV_ARG_ID
import ru.winpenguin.todoapp.navigation.NavigationDestination.MainScreen
import ru.winpenguin.todoapp.navigation.NavigationDestination.SettingsScreen
import ru.winpenguin.todoapp.settings_screen.SettingsScreen
import ru.winpenguin.todoapp.settings_screen.SettingsViewModel
import ru.winpenguin.todoapp.theme.ThemeRepository
import ru.winpenguin.todoapp.theme.ThemeType.DARK
import ru.winpenguin.todoapp.theme.ThemeType.LIGHT
import ru.winpenguin.todoapp.theme.ThemeType.SYSTEM
import ru.winpenguin.todoapp.theme.TodoAppTheme
import ru.winpenguin.todoapp.workers.SyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var networkSyncListener: NetworkSyncListener

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var themeRepository: ThemeRepository

    lateinit var activityComponent: MainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as TodoApp).appComponent
            .mainActivityComponent()
            .create(this)
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContent {
            val themeType by themeRepository.selectedThemeTypeFlow
                .collectAsState(themeRepository.getSelectedThemeType())
            val darkTheme = when (themeType) {
                LIGHT -> false
                DARK -> true
                SYSTEM -> isSystemInDarkTheme()
            }
            Log.d("TAG", "onCreate: themeType $themeType")
            TodoAppTheme(darkTheme = darkTheme) {
                MainNavHost(darkTheme = darkTheme)
            }
            enqueueSync()
            networkSyncListener.start()
        }
    }

    @Composable
    private fun MainNavHost(darkTheme: Boolean) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = MainScreen.destination,
        ) {
            composable(route = MainScreen.destination) {
                MainScreenCompose(
                    viewModel = viewModel(
                        modelClass = MainScreenViewModel::class.java,
                        factory = viewModelFactory
                    ),
                    navigateToDetailsScreen = { id ->
                        navController.navigate("${DetailsScreen.destination}/$id")
                    },
                    navigateToSettingsScreen = {
                        navController.navigate(SettingsScreen.destination)
                    }
                )
            }
            composable(
                route = "${DetailsScreen.destination}/{$NAV_ARG_ID}",
                arguments = DetailsScreen.arguments
            ) { backStackEntry ->
                DetailsComposeScreen(
                    id = backStackEntry.arguments?.getString(NAV_ARG_ID),
                    darkTheme = darkTheme,
                    viewModel = viewModel(
                        modelClass = DetailsScreenViewModel::class.java,
                        factory = viewModelFactory
                    ),
                    navigateBack = {
                        navController.popBackStack(
                            route = MainScreen.destination,
                            inclusive = false
                        )
                    }
                )
            }
            composable(route = SettingsScreen.destination) {
                SettingsScreen(
                    viewModel = viewModel(
                        modelClass = SettingsViewModel::class.java,
                        factory = viewModelFactory
                    ),
                    onCloseClicked = {
                        navController.popBackStack(
                            route = MainScreen.destination,
                            inclusive = false
                        )
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkSyncListener.stop()
    }

    private fun enqueueSync() {
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(8, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SYNC_TODO_ITEMS_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    companion object {
        private const val SYNC_TODO_ITEMS_WORK_NAME = "syncTodoItems"
    }
}