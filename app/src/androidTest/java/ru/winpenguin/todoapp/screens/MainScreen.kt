package ru.winpenguin.todoapp.screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView
import ru.winpenguin.todoapp.R

object MainScreen : KScreen<MainScreen>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val tvTitle = KTextView { withId(R.id.main_title) }
}