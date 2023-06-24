package ru.winpenguin.todoapp.screens

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.check.KCheckBox
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.winpenguin.todoapp.R

object MainScreen : KScreen<MainScreen>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val tvTitle = KTextView { withId(R.id.main_title) }
    val rvTodoList = KRecyclerView(
        builder = { withId(R.id.todo_list) },
        itemTypeBuilder = {
            itemType { TodoItemElement(it) }
        }
    )

    class TodoItemElement(matcher: Matcher<View>) : KRecyclerItem<TodoItemElement>(matcher) {
        val tvText = KTextView { withId(R.id.item_text) }
        val checkbox = KCheckBox { withId(R.id.item_checkbox) }
        val icon = KImageView { withId(R.id.item_icon) }
    }
}