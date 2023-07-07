package ru.winpenguin.todoapp.utils

import java.time.ZoneId
import javax.inject.Inject

class SystemZoneIdProvider @Inject constructor() : ZoneIdProvider {
    override fun zoneId(): () -> ZoneId = { ZoneId.systemDefault() }
}