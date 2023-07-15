package ru.winpenguin.todoapp.data.network

/**
 * Все виды сетевых ошибок
 */
sealed interface NetworkError {
    object ConnectionError : NetworkError

    object AuthorizationError : NetworkError

    object AddItemError : NetworkError

    object UpdateItemError : NetworkError

    object RemoveItemError : NetworkError

    object OtherError : NetworkError
}

const val SC_INCORRECT_REQUEST = 400
const val SC_INCORRECT_AUTHORIZATION = 401
const val SC_ITEM_NOT_FOUND = 404
const val SC_SERVER_ERROR = 500