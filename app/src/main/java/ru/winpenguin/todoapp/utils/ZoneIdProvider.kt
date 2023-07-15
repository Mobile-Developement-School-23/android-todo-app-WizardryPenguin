package ru.winpenguin.todoapp.utils

import java.time.ZoneId

interface ZoneIdProvider {
    fun zoneId(): () -> ZoneId
}