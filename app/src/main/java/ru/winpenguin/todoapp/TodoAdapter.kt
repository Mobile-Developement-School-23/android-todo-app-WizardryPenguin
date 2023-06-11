package ru.winpenguin.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.winpenguin.todoapp.databinding.TodoItemBinding

class TodoAdapter : ListAdapter<TodoItem, TodoItemViewHolder>(DiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private companion object DiffItemCallback : ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem == newItem
        }
    }
}

class TodoItemViewHolder(private val binding: TodoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context: Context
        get() = binding.root.context

    fun bind(model: TodoItem) {
        binding.itemText.text = model.text
        binding.itemCheckbox.isChecked = model.isDone
//        binding.itemCheckbox.buttonTintList = ContextCompat.getColorStateList(context, )
    }
}
