package ru.winpenguin.todoapp.di

import dagger.Binds
import dagger.Module
import ru.winpenguin.todoapp.utils.DefaultLocaleProvider
import ru.winpenguin.todoapp.utils.LocaleProvider
import ru.winpenguin.todoapp.utils.SystemZoneIdProvider
import ru.winpenguin.todoapp.utils.ZoneIdProvider

@Module
abstract class DateTimeModule {

    @Binds
    abstract fun bindLocaleProvider(impl: DefaultLocaleProvider): LocaleProvider

    @Binds
    abstract fun bindZoneIdProvider(impl: SystemZoneIdProvider): ZoneIdProvider
}