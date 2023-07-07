package ru.winpenguin.todoapp.di

import dagger.Module
import ru.winpenguin.todoapp.di.main_activity_component.MainActivityComponent

@Module(subcomponents = [MainActivityComponent::class])
object SubcomponentsModule {
}