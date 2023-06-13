package ru.winpenguin.todoapp.domain.models

import java.time.LocalDate

sealed class Deadline {
    class NotSelected : Deadline()
    class Selected(val date: LocalDate) : Deadline()
}