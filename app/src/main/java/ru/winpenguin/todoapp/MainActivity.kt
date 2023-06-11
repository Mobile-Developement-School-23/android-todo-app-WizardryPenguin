package ru.winpenguin.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.winpenguin.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val todoItemsRepository: TodoItemsRepository
        get() = (application as TodoApp).todoItemsRepository

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter = TodoAdapter()
        todoAdapter.submitList(todoItemsRepository.getItems())
        binding.todoList.adapter = todoAdapter
    }
}