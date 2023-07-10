package ru.winpenguin.todoapp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavigationDestination(val destination: String) {

    object MainScreen : NavigationDestination("MainScreen")

    object DetailsScreen : NavigationDestination("DetailsScreen") {
        const val NAV_ARG_ID = "id"

        val arguments = listOf(
            navArgument(NAV_ARG_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        )
    }

    object SettingsScreen : NavigationDestination("settings")
}

