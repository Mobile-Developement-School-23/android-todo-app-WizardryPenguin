package ru.winpenguin.todoapp

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import ru.winpenguin.todoapp.screens.MainScreen

class MainScreenTest : TestCase() {

    @get:Rule
    val activityScenario = activityScenarioRule<MainActivity>()

    @Test
    fun checkInitialState() {
        run {
            step("Проверяем отображение заголовка") {
                MainScreen {
                    tvTitle {
                        isVisible()
                        hasText(R.string.my_business)
                    }
                }
            }
            step("Проверяем начальный список дел") {
                MainScreen {
                    rvTodoList {
                        assertEquals(12, getSize())
                    }
                }
            }
        }
    }
}