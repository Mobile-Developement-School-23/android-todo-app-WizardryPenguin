package ru.winpenguin.todoapp.main_screen.ui

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.databinding.TodoItemBinding
import ru.winpenguin.todoapp.utils.getColorFromAttr

private const val ITEM_TYPE_ONLY = 1
private const val ITEM_TYPE_FIRST = 2
private const val ITEM_TYPE_LAST = 3
private const val ITEM_TYPE_MIDDLE = 4

class TodoAdapter(
    private val onItemChecked: (String, Boolean) -> Unit,
    private val onItemClicked: (String) -> Unit,
) : ListAdapter<TodoItemUiState, TodoItemViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return when (viewType) {
            ITEM_TYPE_ONLY -> OnlyTodoItemViewHolder(binding)
            ITEM_TYPE_FIRST -> FirstTodoItemViewHolder(binding)
            ITEM_TYPE_LAST -> LastTodoItemViewHolder(binding)
            ITEM_TYPE_MIDDLE -> MiddleTodoItemViewHolder(binding)
            else -> throw IllegalArgumentException("Unknown view type in TodoAdapter: $viewType")
        }
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            state = item,
            onItemChecked = onItemChecked,
            onItemClicked = onItemClicked
        )
    }

    private companion object DiffItemCallback : ItemCallback<TodoItemUiState>() {
        override fun areItemsTheSame(oldItem: TodoItemUiState, newItem: TodoItemUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TodoItemUiState,
            newItem: TodoItemUiState,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            itemCount == 1 -> ITEM_TYPE_ONLY
            position == 0 -> ITEM_TYPE_FIRST
            position == itemCount - 1 -> ITEM_TYPE_LAST
            else -> ITEM_TYPE_MIDDLE
        }
    }
}

abstract class TodoItemViewHolder(private val binding: TodoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context: Context
        get() = binding.root.context

    fun bind(
        state: TodoItemUiState,
        onItemChecked: (String, Boolean) -> Unit,
        onItemClicked: (String) -> Unit
    ) {
        with(binding) {
            itemCheckbox.isChecked = state.isChecked
            itemCheckbox.buttonTintList =
                ContextCompat.getColorStateList(context, state.checkBoxColorRes)
            itemCheckbox.setOnClickListener {
                onItemChecked(state.id, itemCheckbox.isChecked)
            }

            root.setBackgroundResource(getBackgroundRes())
            root.setOnClickListener {
                onItemClicked(state.id)
            }

            itemText.setTextColor(context.getColorFromAttr(state.textColorAttr))
            if (state.isStrikedThrough) {
                itemText.paintFlags = itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                itemText.paintFlags = itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            itemText.text = state.text

            if (state.priorityIconRes != null) {
                priorityIcon.isVisible = true
                priorityIcon.setImageResource(state.priorityIconRes)
            } else {
                priorityIcon.isVisible = false
            }

            additionalText.text = state.additionalText
            additionalText.isVisible = !state.additionalText.isNullOrBlank()
        }
    }

    @DrawableRes
    abstract fun getBackgroundRes(): Int
}

class OnlyTodoItemViewHolder(binding: TodoItemBinding) : TodoItemViewHolder(binding) {

    override fun getBackgroundRes(): Int {
        return R.drawable.round_corners_bg
    }
}

class FirstTodoItemViewHolder(binding: TodoItemBinding) : TodoItemViewHolder(binding) {

    override fun getBackgroundRes(): Int {
        return R.drawable.round_top_corners_bg
    }
}

class LastTodoItemViewHolder(binding: TodoItemBinding) : TodoItemViewHolder(binding) {

    override fun getBackgroundRes(): Int {
        return R.drawable.round_bottom_corners_bg
    }
}

class MiddleTodoItemViewHolder(binding: TodoItemBinding) : TodoItemViewHolder(binding) {

    override fun getBackgroundRes(): Int {
        return R.drawable.rectangle_bg
    }
}
